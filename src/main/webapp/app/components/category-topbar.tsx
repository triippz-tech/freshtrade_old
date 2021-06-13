import React from 'react';

import { Row, Button, Collapse } from 'reactstrap';
import Filters from 'app/components/filters';
import SortBy, { SortType } from 'app/components/sort-by';
import Icon from './icon';
import { ITEMS_PER_PAGE } from 'app/shared/util/pagination.constants';

interface CategoryTopBarProps {
  filter?: boolean;
  onResultClick?: (size: number) => void;
  resultSize?: number;
  totalItems?: number;
  onClear?: () => void;
  onSortSelect?: (selectedValue: SortType) => void;
}

const defaultProps: CategoryTopBarProps = {
  filter: false,
};

const CategoryTopBar = (props: CategoryTopBarProps) => {
  const [collapse, setCollapse] = React.useState(false);
  const [activeResult, setActiveResult] = React.useState(1);

  const onResultClick = (active, size) => {
    setActiveResult(active);
    props.onResultClick(size);
  };

  return (
    <header className="product-grid-header">
      <div className="mr-3 mb-3">
        Showing <strong>1-{props.resultSize} </strong>of <strong>{props.totalItems} </strong>items
      </div>
      <div className="mr-3 mb-3">
        <span className="mr-2">Show</span>
        <Button
          color={activeResult === 0 ? 'primary' : 'link'}
          outline={activeResult === 0}
          className={`product-grid-header-show`}
          onClick={() => onResultClick(0, 12)}
        >
          12
        </Button>
        <Button
          color={activeResult === 1 ? 'primary' : 'link'}
          outline={activeResult === 1}
          className={`product-grid-header-show`}
          onClick={() => onResultClick(1, ITEMS_PER_PAGE)}
        >
          {ITEMS_PER_PAGE}
        </Button>
      </div>
      {props.filter && (
        <div className="mr-3 mb-3">
          <Button
            color="link"
            className="text-dark pl-0 dropdown-toggle text-decoration-none"
            data-toggle="collapse"
            aria-expanded={collapse}
            onClick={() => setCollapse(!collapse)}
          >
            Filter
          </Button>
        </div>
      )}

      <div className="mb-3 d-flex align-items-center">
        <span className="d-inline-block mr-2">Sort by</span>
        <SortBy onSelect={props.onSortSelect} />
      </div>
      {props.filter && (
        <Collapse isOpen={collapse} className="w-100">
          <div className="py-4 mb-5">
            <Row>
              <Filters top={true} />
            </Row>
            <Button color="link" className="d-flex align-items-center pl-0 ml-n3" onClick={() => props.onClear()}>
              <Icon icon="close-1" className="w-3rem h-3rem mr-n1" />
              Clear all filters
            </Button>
          </div>
        </Collapse>
      )}
    </header>
  );
};

CategoryTopBar.defaultProps = defaultProps;

export default CategoryTopBar;
