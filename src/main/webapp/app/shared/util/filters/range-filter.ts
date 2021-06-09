import { Filter, IFilter } from 'app/shared/util/filters/filter';

export interface IComparable<T> {
  compareTo(o: T): number;
}

export interface IRangeFilter<FIELD_TYPE> {
  greaterThan?: any;
  lessThan?: any;
  greaterThanOrEqual?: any;
  lessThanOrEqual?: any;
  filter: IFilter<FIELD_TYPE>;
}

export abstract class RangeFilter<FIELD_TYPE> extends Filter<FIELD_TYPE> {
  protected _greaterThan: FIELD_TYPE;
  protected _lessThan: FIELD_TYPE;
  protected _greaterThanOrEqual: FIELD_TYPE;
  protected _lessThanOrEqual: FIELD_TYPE;

  protected constructor(obj: IRangeFilter<FIELD_TYPE>) {
    super(obj.filter);
    this._greaterThan = obj.greaterThan === undefined ? null : obj.greaterThan;
    this._lessThan = obj.lessThan === undefined ? null : obj.greaterThan;
    this._greaterThanOrEqual = obj.greaterThanOrEqual === undefined ? null : obj.greaterThanOrEqual;
    this._lessThanOrEqual = obj.lessThanOrEqual === undefined ? null : obj.lessThanOrEqual;
  }

  get greaterThan(): FIELD_TYPE {
    return this._greaterThan;
  }

  set greaterThan(value: FIELD_TYPE) {
    this._greaterThan = value;
  }

  get lessThan(): FIELD_TYPE {
    return this._lessThan;
  }

  set lessThan(value: FIELD_TYPE) {
    this._lessThan = value;
  }

  get greaterThanOrEqual(): FIELD_TYPE {
    return this._greaterThanOrEqual;
  }

  set greaterThanOrEqual(value: FIELD_TYPE) {
    this._greaterThanOrEqual = value;
  }

  get lessThanOrEqual(): FIELD_TYPE {
    return this._lessThanOrEqual;
  }

  set lessThanOrEqual(value: FIELD_TYPE) {
    this._lessThanOrEqual = value;
  }
}
