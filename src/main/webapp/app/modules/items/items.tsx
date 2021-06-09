import React, { useEffect, useState } from 'react';
import { RouteComponentProps } from 'react-router-dom';
import { IRootState } from 'app/shared/reducers';
import { getListPageEntities, reset } from 'app/entities/item/item.reducer';
import { connect } from 'react-redux';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { getSortState } from 'react-jhipster';
import { ITEMS_PER_PAGE } from 'app/shared/util/pagination.constants';
import { MDBCol, MDBRow } from 'mdbreact';
import ItemTile from 'app/components/items/item-tile';
import { defaultFilter, IItemCriteria, ItemCriteria } from 'app/shared/criteria/item-criteria';
import { StringFilter } from 'app/shared/util/filters/string-filter';

export interface IItemsProps extends StateProps, DispatchProps, RouteComponentProps<{ slug: string }> {}

export const Items = (props: IItemsProps) => {
  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getSortState(props.location, ITEMS_PER_PAGE, 'id'), props.location.search)
  );
  const [sorting, setSorting] = useState(false);

  const buildCriteria = () => {
    const criteria: IItemCriteria = defaultFilter;
    if (props.match.params.slug !== undefined) {
      criteria.categorySlug = new StringFilter({
        variableName: 'categorySlug',
        contains: null,
        doesNotContain: null,
        filter: {
          equals: props.match.params.slug,
        },
      });
    }
    return criteria;
  };

  const getAllEntities = () => {
    let criteria = new ItemCriteria(buildCriteria());
    if (props.match.params.slug === undefined) criteria = null;
    props.getListPageEntities(
      criteria,
      paginationState.activePage - 1,
      paginationState.itemsPerPage,
      `${paginationState.sort},${paginationState.order}`
    );
  };

  const resetAll = () => {
    props.reset();
    setPaginationState({
      ...paginationState,
      activePage: 1,
    });
    let criteria = new ItemCriteria(buildCriteria());
    if (props.match.params.slug === undefined) criteria = null;
    props.getListPageEntities(
      criteria,
      paginationState.activePage - 1,
      paginationState.itemsPerPage,
      `${paginationState.sort},${paginationState.order}`
    );
  };

  useEffect(() => {
    resetAll();
  }, []);

  useEffect(() => {
    getAllEntities();
  }, [paginationState.activePage, props.match.params.slug]);

  const handleLoadMore = () => {
    if ((window as any).pageYOffset > 0) {
      setPaginationState({
        ...paginationState,
        activePage: paginationState.activePage + 1,
      });
    }
  };

  useEffect(() => {
    if (sorting) {
      getAllEntities();
      setSorting(false);
    }
  }, [sorting]);

  const sort = p => () => {
    props.reset();
    setPaginationState({
      ...paginationState,
      activePage: 1,
      order: paginationState.order === 'asc' ? 'desc' : 'asc',
      sort: p,
    });
    setSorting(true);
  };

  const handleSyncList = () => {
    resetAll();
  };

  const { itemList, match, loading } = props;

  return (
    <div className="section">
      <h4 className="text-center mt-4 mb-5">
        <strong>Items</strong>
      </h4>
      <MDBRow>
        {props.itemList.map((item, idx) => {
          return (
            <MDBCol lg="2" md="12" className="mb-4 " key={`item-${idx}`}>
              <ItemTile item={item} />
            </MDBCol>
          );
        })}
      </MDBRow>
    </div>
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

export default connect(mapStateToProps, mapDispatchToProps)(Items);
