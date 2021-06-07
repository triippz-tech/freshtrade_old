import { IRangeFilter, RangeFilter } from 'app/shared/util/filters/range-filter';
import { dateFormat } from 'app/shared/util/date-utils';

export interface IDateFilter {
  variableName: string;
  rangeFilter: IRangeFilter<Date>;
}

export class DateFilter extends RangeFilter<Date> {
  private _variableName: string;

  constructor(obj: IDateFilter) {
    super(obj.rangeFilter);
    this._variableName = obj.variableName;
  }

  public generateFilter() {
    let strFilter = '';
    if (this._specified !== null) strFilter += `${this.variableName}.specified=${this._specified}&`;
    if (this._notEquals !== null) strFilter += `${this.variableName}.notEquals=${dateFormat(this._notEquals, 'YYYY-MM-DD')}&`;
    if (this._equals !== null) strFilter += `${this.variableName}.equals=${dateFormat(this._equals, 'YYYY-MM-DD')}&`;
    if (this._greaterThan !== null) strFilter += `${this.variableName}.greaterThan=${dateFormat(this._greaterThan, 'YYYY-MM-DD')}&`;
    if (this._lessThan !== null) strFilter += `${this.variableName}.lessThan=${dateFormat(this._lessThan, 'YYYY-MM-DD')}&`;
    if (this._greaterThanOrEqual !== null)
      strFilter += `${this.variableName}.greaterThanOrEqual=${dateFormat(this._greaterThanOrEqual, 'YYYY-MM-DD')}&`;
    if (this._lessThanOrEqual !== null)
      strFilter += `${this.variableName}.lessThanOrEqual=${dateFormat(this._lessThanOrEqual, 'YYYY-MM-DD')}&`;

    if (this._in !== null) {
      this._in.forEach(value => {
        strFilter += `${this._variableName}.in=${dateFormat(value, 'YYYY-MM-DD')}&`;
      });
    }
    return strFilter;
  }

  get variableName(): string {
    return this._variableName;
  }

  set variableName(value: string) {
    this._variableName = value;
  }
}
