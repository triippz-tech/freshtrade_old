import { toTitleCase } from 'app/shared/util/string-utils';

export const enumToArray = (enumVals): Array<string> => {
  const generatedValues: Array<string> = Object.values(enumVals);
  return generatedValues.map(value => toTitleCase(value));
};

export const formatPrice = (price: number | null): string => {
  if (price)
    return price.toLocaleString('en-US', {
      style: 'currency',
      currency: 'USD',
    });
  return '$0.00';
};
