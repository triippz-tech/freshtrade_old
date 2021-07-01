import { toTitleCase } from 'app/shared/util/string-utils';

export const enumToArray = (enumVals): Array<string> => {
  const generatedValues: Array<string> = Object.values(enumVals);
  return generatedValues.map(value => toTitleCase(value));
};

export const formatPrice = (price: number): string =>
  price.toLocaleString('en-US', {
    style: 'currency',
    currency: 'USD',
  });
