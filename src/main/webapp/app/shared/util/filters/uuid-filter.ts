import { Filter, IFilter } from 'app/shared/util/filters/filter';

export interface IUUIDFilter {
  variableName: string;
  filter: IFilter<string>;
}

export class UUIDFilter extends Filter<string> {
  private _variableName: string;

  constructor(obj: IUUIDFilter) {
    super(obj.filter);
    this._variableName = obj.variableName;
  }

  public generateFilter() {
    let strFilter = '';
    if (this._specified !== null) strFilter += `${this.variableName}.specified=${this._specified}&`;
    if (this._notEquals !== null) strFilter += `${this.variableName}.notEquals=${this._notEquals}&`;
    if (this._equals !== null) strFilter += `${this.variableName}.equals=${this._equals}&`;

    if (this._in !== null) {
      this._in.forEach(value => {
        strFilter += `${this._variableName}.in=${value}&`;
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
