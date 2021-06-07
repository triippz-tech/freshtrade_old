import { IRangeFilter, RangeFilter } from 'app/shared/util/filters/range-filter';

export interface INumberFilter {
  variableName: string;
  rangeFilter: IRangeFilter<number>;
}

export class NumberFilter extends RangeFilter<number> {
  private _variableName: string;

  constructor(obj: INumberFilter) {
    super(obj.rangeFilter);
    this._variableName = obj.variableName;
  }

  get variableName(): string {
    return this._variableName;
  }

  set variableName(value: string) {
    this._variableName = value;
  }

  public generateFilter() {
    let strFilter = '';
    if (this._specified !== null) strFilter += `${this.variableName}.specified=${this._specified}&`;
    if (this._notEquals !== null) strFilter += `${this.variableName}.notEquals=${this._notEquals}&`;
    if (this._equals !== null) strFilter += `${this.variableName}.equals=${this._equals}&`;
    if (this._greaterThan !== null) strFilter += `${this.variableName}.greaterThan=${this._greaterThan}&`;
    if (this._lessThan !== null) strFilter += `${this.variableName}.lessThan=${this._lessThan}&`;
    if (this._greaterThanOrEqual !== null) strFilter += `${this.variableName}.greaterThanOrEqual=${this._greaterThanOrEqual}&`;
    if (this._lessThanOrEqual !== null) strFilter += `${this.variableName}.lessThanOrEqual=${this._lessThanOrEqual}&`;

    if (this._in !== null) {
      this._in.forEach(value => {
        strFilter += `${this._variableName}.in=${value}&`;
      });
    }
    return strFilter;
  }
}
