import React, { useState } from 'react';

import { Col, Collapse, FormGroup, Form, CustomInput, Label, Input, InputGroupAddon, InputGroupText, Row, Button } from 'reactstrap';

import brands from 'app/shared/data/brands.json';
import tags from 'app/shared/data/tags.json';
import colors from 'app/shared/data/colors.json';
import PriceSlider from 'app/components/price-slider';
import { Condition } from 'app/shared/model/enumerations/condition.model';
import { enumToArray } from 'app/shared/util/transform-utils';
import { toEnumCase } from 'app/shared/util/string-utils';
import NumberInput from 'app/components/number-input';

interface FiltersProps {
  onConditionChange?: (condition: Condition) => void;
  onPriceRangeChange?: () => void;
  onNumberChange?: (name: string, value: number) => void;
  clearFilters?: () => void;
  top?: boolean;
  minPrice?: number;
  maxPrice?: number;
  minPriceValue?: number;
  maxPriceValue?: number;
}

const defaultProps: FiltersProps = {
  top: false,
  minPrice: 0,
  maxPrice: 1000000000,
};

const PriceFilter = ({ top, minPrice, maxPrice, minPriceValue, maxPriceValue, onKeyPress, onFilterPress, onNumberChange }) => {
  return (
    <Form className={top ? '' : 'mt-4 mt-lg-0'}>
      <FormGroup className="mb-1">
        <Row>
          <Col>
            <Label for="minPrice">Min Price</Label>
            <InputGroupAddon addonType="prepend">
              <InputGroupText>$</InputGroupText>
              <NumberInput
                autoFocus
                value={minPriceValue}
                name="minPrice"
                type="number"
                min={minPrice}
                max={maxPrice}
                placeholder="Min"
                onPressEnter={onKeyPress}
                onChange={value => onNumberChange('minPrice', value)}
              />
            </InputGroupAddon>
          </Col>
          <Col>
            <Label for="maxPrice">Max Price</Label>
            <InputGroupAddon addonType="prepend">
              <InputGroupText>$</InputGroupText>
              <NumberInput
                autoFocus
                value={maxPriceValue}
                name="maxPrice"
                type="number"
                min={minPrice}
                max={maxPrice}
                placeholder="Max"
                onPressEnter={onKeyPress}
                onChange={value => onNumberChange('maxPrice', value)}
              />
            </InputGroupAddon>
          </Col>
        </Row>
        <Row className="justify-content-center align-items-center mt-1">
          <Button onClick={() => onFilterPress()}>Filter</Button>
        </Row>
      </FormGroup>
    </Form>
  );
};

// Condition filter component
const ConditionFilter = ({ filterInputs, top, onInputChange }) => (
  <Form className={top ? '' : 'mt-4 mt-lg-0'}>
    {enumToArray(Condition).map(condition => (
      <FormGroup key={condition} className="mb-1">
        <CustomInput
          type="radio"
          name="condition"
          id={condition}
          label={condition}
          // @ts-ignore
          checked={filterInputs['condition'] ? filterInputs['condition'] === condition : ''}
          onChange={e => onInputChange(e)}
        />
      </FormGroup>
    ))}
  </Form>
);

const Filters = (props: FiltersProps): any => {
  // const [minPrice, setMinPrice] = useState(0);
  // const [maxPrice, setMaxPrice] = useState(0);

  // Filter inputs state where all inputs all stored.
  const [filterInputs, setFilterInputs] = React.useState({
    // Remove or customize on PROD - Some brands are preselected by "checked" property. Using reduce they're set default in state.
    'clothes-brand': brands.reduce((a, item) => (item.checked && a.push(item.value), a), []),

    // Remove or customize on PROD - Set first Condition as default
    condition: enumToArray(Condition)[0],

    // Remove or customize on PROD - Set first tag as default
    tag: [tags[0].value],
  });

  // Collapse state
  const [collapse, setCollapse] = React.useState({});
  const toggleCollapse = name => {
    setCollapse({ ...collapse, [name]: !collapse[name] });
  };

  // On input change func
  const onInputChange = e => {
    const type = e.target.type; // Input type
    const value = e.target.id; // Input value -> the enum in title case
    const name = e.target.name; // Input name

    if (type === 'radio') {
      setFilterInputs({ ...filterInputs, [name]: value });
      props.onConditionChange(Condition[toEnumCase(value)]);
    } else {
      // If not input type radio
      filterInputs[name] // If input group exists
        ? filterInputs[name].some(item => item === value) // If item exists in array > remove
          ? setFilterInputs({ ...filterInputs, [name]: filterInputs[name].filter(x => x !== value) })
          : setFilterInputs({ ...filterInputs, [name]: [...filterInputs[name], value] }) // If item doesn't exists in array > add it to input group
        : setFilterInputs({ ...filterInputs, [name]: [value] }); // If input group doesn't exists > create input group and add value
    }
  };

  // Checkbox filter component
  const CheckboxesFilter = ({ data }) => (
    <Form className={props.top ? '' : 'mt-4 mt-lg-0'}>
      {data.map(item => (
        <FormGroup key={item.value} className="mb-1">
          <CustomInput
            type="checkbox"
            name={item.name}
            id={item.value}
            label={
              <React.Fragment>
                {item.label} <small>({item.productcount})</small>
              </React.Fragment>
            }
            checked={filterInputs[item.name] ? filterInputs[item.name].includes(item.value) : ''}
            onChange={e => onInputChange(e)}
          />
        </FormGroup>
      ))}
    </Form>
  );

  // Colors filter component
  const ColorsFilter = () => (
    <ul className={`list-inline mb-0 colours-wrapper ${props.top ? '' : 'mt-4 mt-lg-0'}`}>
      {colors.map(color => (
        <li key={color.value} className="list-inline-item">
          <Label
            className={`btn-colour ${filterInputs['colour'] && filterInputs['colour'].some(item => item === color.value) ? 'active' : ''}`}
            for={color.value}
            style={{ backgroundColor: color.color }}
          />
          <Input
            className="input-invisible"
            type="checkbox"
            name="colour"
            id={color.value}
            checked={filterInputs['colour'] ? filterInputs['colour'].includes(color.value) : ''}
            onChange={e => onInputChange(e)}
          />
        </li>
      ))}
    </ul>
  );

  // const onPriceRangeChange = (name, value) => {
  //   if (name === "minPrice") setMinPrice(value);
  //   else setMaxPrice(value);
  // }

  const onKeyPress = e => {
    if (e.key === 'Enter') {
      const value = +e.target.value;
      if (isNaN(value)) return;
      if (value < props.minPrice || value > props.maxPrice) return;
      props.onPriceRangeChange();
    }
  };

  // Filters for sidebar
  const sidebarFilters = [
    {
      component: (
        <Button color="primary" onClick={() => props.clearFilters()}>
          Clear
        </Button>
      ),
      title: 'Clear Filters',
      subtitle: 'Clear Filters',
    },
    {
      component: (
        <PriceFilter
          minPrice={props.minPrice}
          maxPrice={props.maxPrice}
          minPriceValue={props.minPriceValue}
          maxPriceValue={props.maxPriceValue}
          onNumberChange={props.onNumberChange}
          onFilterPress={props.onPriceRangeChange}
          onKeyPress={onKeyPress}
          top={props.top}
        />
      ),
      title: 'Filter by price',
      subtitle: 'Price',
    },
    {
      component: <ConditionFilter top={props.top} onInputChange={onInputChange} filterInputs={filterInputs} />,
      title: 'Filter by Item Condition',
      subtitle: 'Condition',
    },
  ];

  // Filters above product
  const topFilters: any = [
    {
      component: (
        <PriceFilter
          minPrice={props.minPrice}
          maxPrice={props.maxPrice}
          minPriceValue={props.minPriceValue}
          maxPriceValue={props.maxPriceValue}
          onNumberChange={props.onNumberChange}
          onFilterPress={props.onPriceRangeChange}
          onKeyPress={onKeyPress}
          top={props.top}
        />
      ),
      title: 'Filter by price',
      subtitle: 'Price',
    },
    {
      component: <ConditionFilter top={props.top} onInputChange={onInputChange} filterInputs={filterInputs} />,
      title: 'Filter by Item Condition',
      subtitle: 'Condition',
    },
  ];

  // If top prop true, set filters above products else to sidebar
  const filtersBlocks = props.top ? topFilters : sidebarFilters;

  return filtersBlocks.map((filter, index) =>
    props.top ? ( // If top filters position
      <Col key={index} sm="6" xl="3" className="mb-3">
        {Array.isArray(filter) ? ( // If multiple filters in same column
          filter.map(item => (
            <React.Fragment key={item.subtitle}>
              <h6 className="text-dark">{item.subtitle}</h6>

              {/* FILTER */}
              {item.component}
              {/* END FILTER */}
            </React.Fragment>
          ))
        ) : (
          <React.Fragment>
            <h6 className="text-dark">{filter.subtitle}</h6>

            {/* FILTER */}
            {filter.component}
            {/* END FILTER */}
          </React.Fragment>
        )}
      </Col>
    ) : (
      <div key={index} className="sidebar-block px-3 px-lg-0">
        {/* COLLAPSE TOGGLE */}
        <a
          className="d-lg-none block-toggler"
          data-toggle="collapse"
          aria-expanded={collapse[filter.subtitle]}
          onClick={() => toggleCollapse(filter.subtitle)}
        >
          {filter.title}
          <span className="block-toggler-icon" />
        </a>
        {/* END COLLAPSE TOGGLE */}

        {/* COLLAPSE */}
        <Collapse isOpen={collapse[filter.subtitle]} className="expand-lg">
          <h5 className="sidebar-heading d-none d-lg-block">{filter.subtitle}</h5>

          {/* FILTER */}
          {filter.component}
          {/* END FILTER */}
        </Collapse>
        {/* END COLLAPSE */}
      </div>
    )
  );
};

Filters.defaultProps = defaultProps;

export default Filters;
