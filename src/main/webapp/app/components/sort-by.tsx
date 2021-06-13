import React from 'react';
import SelectBox from 'app/components/select-box';

export enum SortType {
  A_Z,
  Z_A,
  PRICE_LOW_TO_HIGH,
  PRICE_HIGH_TO_LOW,
  NEWEST,
}

interface SortByProps {
  onSelect: (selectedValue: SortType) => void;
}

const SortBy = (props: SortByProps) => {
  const options = [
    {
      value: SortType.A_Z,
      label: 'A-Z',
    },
    {
      value: SortType.Z_A,
      label: 'Z-A',
    },
    {
      value: SortType.PRICE_LOW_TO_HIGH,
      label: 'Price (Low to High)',
    },
    {
      value: SortType.PRICE_HIGH_TO_LOW,
      label: 'Price (High to Low)',
    },
    {
      value: SortType.NEWEST,
      label: 'Newest',
    },
  ];

  return <SelectBox options={options} onChange={res => props.onSelect(res.value)} />;
};

export default SortBy;
