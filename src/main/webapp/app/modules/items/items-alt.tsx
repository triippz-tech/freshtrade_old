import React, { useEffect, useState } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { IRootState } from 'app/shared/reducers';
import InfiniteScroll from 'react-infinite-scroller';
import { getListPageEntities, reset } from 'app/entities/item/item.reducer';
import { connect } from 'react-redux';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { getSortState } from 'react-jhipster';
import { ITEMS_PER_PAGE } from 'app/shared/util/pagination.constants';
import { defaultFilter, IItemCriteria, ItemCriteria } from 'app/shared/criteria/item-criteria';
import { StringFilter } from 'app/shared/util/filters/string-filter';
import { Breadcrumb, BreadcrumbItem, Col, Container, Row, Spinner } from 'reactstrap';
import CardProduct from 'app/components/card-product';
import CategoryTopBar from 'app/components/category-topbar';
import Filters from 'app/components/filters';
import { SortType } from 'app/components/sort-by';
import { Condition } from 'app/shared/model/enumerations/condition.model';
import { ConditionFilter } from 'app/shared/util/filters/enum/condition-filter';
import { NumberFilter } from 'app/shared/util/filters/number-filter';

export interface IItemsProps extends StateProps, DispatchProps, RouteComponentProps<{ slug: string }> {}

export const ItemsAlt = (props: IItemsProps) => {
  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getSortState(props.location, ITEMS_PER_PAGE, 'id'), props.location.search)
  );
  const [resultsPerPage, setResultsPerPage] = useState(ITEMS_PER_PAGE);
  const [condition, setCondition] = useState<Condition>(null);
  const [{ minPrice, maxPrice }, setPriceRange] = useState({
    minPrice: 0,
    maxPrice: 0,
  });

  const buildCriteria = () => {
    const criteria: IItemCriteria = defaultFilter;
    if (props.match.params.slug !== undefined) {
      criteria.categorySlug = new StringFilter({
        variableName: 'categorySlug',
        contains: null,
        doesNotContain: null,
        filter: { equals: props.match.params.slug },
      });
    }
    criteria.itemCondition =
      condition === null
        ? null
        : new ConditionFilter({
            filter: { equals: condition },
          });
    criteria.price =
      minPrice === 0 && maxPrice === 0
        ? null
        : new NumberFilter({
            variableName: 'price',
            rangeFilter: {
              greaterThanOrEqual: minPrice,
              lessThanOrEqual: maxPrice,
              filter: null,
            },
          });
    return criteria;
  };

  const getAllEntities = () => {
    props.reset();
    const criteria = new ItemCriteria(buildCriteria());
    props.getListPageEntities(criteria, paginationState.activePage - 1, resultsPerPage, `${paginationState.sort},${paginationState.order}`);
  };

  const resetAll = () => {
    props.reset();
    setPaginationState({
      ...paginationState,
      activePage: 1,
    });
    let criteria = new ItemCriteria(buildCriteria());
    if (props.match.params.slug === undefined) criteria = null;
    props.getListPageEntities(criteria, paginationState.activePage - 1, resultsPerPage, `${paginationState.sort},${paginationState.order}`);
  };

  useEffect(() => {
    resetAll();
  }, []);

  useEffect(() => {
    getAllEntities();
  }, [paginationState.activePage, resultsPerPage, paginationState.sort, paginationState.order, condition]);

  useEffect(() => {
    // Used if there is a filter clear
    if (minPrice === 0 && maxPrice === 0) getAllEntities();
  }, [minPrice, maxPrice]);

  useEffect(() => {
    // If we change categories
    clearFilters();
  }, [props.match.params.slug]);

  const handleLoadMore = () => {
    if ((window as any).pageYOffset > 0) {
      setPaginationState({
        ...paginationState,
        activePage: paginationState.activePage + 1,
      });
    }
  };

  const sort = (field, order) => {
    props.reset();
    /* eslint object-shorthand: 0 */
    setPaginationState({
      ...paginationState,
      activePage: 1,
      order: order,
      sort: field,
    });
  };

  const handleSyncList = () => {
    resetAll();
  };

  const clearFilters = () => {
    setCondition(null);
    setPriceRange({ minPrice: 0, maxPrice: 0 });
  };

  const onResultSizeClick = (size: number) => setResultsPerPage(size);

  const onSortBySelect = (selectedValue: SortType) => {
    if (selectedValue === SortType.A_Z) sort('name', 'asc');
    else if (selectedValue === SortType.Z_A) sort('name', 'desc');
    else if (selectedValue === SortType.NEWEST) sort('createdDate', 'asc');
    else if (selectedValue === SortType.PRICE_HIGH_TO_LOW) sort('price', 'desc');
    else if (selectedValue === SortType.PRICE_LOW_TO_HIGH) sort('price', 'asc');
  };

  const onConditionChange = (newCondition: Condition) => setCondition(newCondition);

  const { itemList, match, loading } = props;

  return (
    <Container fluid className="py-2">
      <Breadcrumb>
        <BreadcrumbItem>
          <Link to="/">
            <a>Home</a>
          </Link>
        </BreadcrumbItem>
        <BreadcrumbItem active>{props.match.params.slug ? props.match.params.slug.toUpperCase() : 'ITEMS'}</BreadcrumbItem>
      </Breadcrumb>
      <div className="hero-content pb-4">
        <h1>{props.match.params.slug ? props.match.params.slug.toUpperCase() : 'ITEMS'}</h1>
      </div>
      <Row>
        <Col xl="3" lg="4" className="sidebar pr-xl-5 order-lg-1">
          {/* <CategoriesMenu /> */}
          <Filters
            key={'FILTERS'}
            onConditionChange={onConditionChange}
            clearFilters={() => clearFilters()}
            minPrice={0}
            maxPrice={100000000}
            minPriceValue={minPrice}
            maxPriceValue={maxPrice}
            onNumberChange={(name, value) =>
              setPriceRange(prevState => ({
                ...prevState,
                [name]: value,
              }))
            }
            onPriceRangeChange={() => getAllEntities()}
          />
        </Col>
        <Col xl="9" lg="8" className="products-grid order-lg-2">
          <CategoryTopBar
            totalItems={props.totalItems}
            resultSize={props.itemList.length}
            onResultClick={onResultSizeClick}
            onSortSelect={onSortBySelect}
          />
          <InfiniteScroll
            pageStart={paginationState.activePage}
            loadMore={handleLoadMore}
            hasMore={paginationState.activePage - 1 < props.links.next}
            loader={
              <div className="text-center">
                <Spinner color="primary" size="xl" />
              </div>
            }
            threshold={0}
            initialLoad={false}
          >
            <Row>
              {itemList.length < 3 &&
                itemList.map((val, index) => (
                  <Col key={index} md="4">
                    <CardProduct product={val} />
                  </Col>
                ))}
              {itemList.slice(0, -2).map((product, index) => (
                <Col key={index} sm="4" xl="2" xs="6">
                  <CardProduct product={product} />
                </Col>
              ))}
            </Row>
          </InfiniteScroll>
        </Col>
      </Row>
    </Container>
  );
};

const mapStateToProps = ({ item }: IRootState) => ({
  itemList: item.entities,
  loading: item.loading,
  totalItems: item.totalItems,
  links: item.links,
});

const mapDispatchToProps = {
  getListPageEntities,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(ItemsAlt);
