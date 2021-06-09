import { Filter, IFilter } from 'app/shared/util/filters/filter';

export interface IStringFilter {
  variableName: string;
  contains?: string | null;
  doesNotContain?: string | null;
  filter: IFilter<string>;
}

export class StringFilter extends Filter<string> {
  private _variableName: string;
  private _contains: string = null;
  private _doesNotContain: string = null;

  constructor(obj: IStringFilter) {
    super(obj.filter);
    if (obj.contains !== undefined) {
      this._contains = obj.contains;
    }
    if (obj.doesNotContain !== undefined) {
      this._doesNotContain = obj.doesNotContain;
    }
    this._variableName = obj.variableName;
  }

  public generateFilter() {
    let strFilter = '';
    if (this._specified !== undefined && this._specified !== null) strFilter += `${this.variableName}.specified=${this._specified}&`;
    if (this._notEquals !== undefined && this._notEquals !== null) strFilter += `${this.variableName}.notEquals=${this._notEquals}&`;
    if (this._equals !== undefined && this._equals !== null) strFilter += `${this.variableName}.equals=${this._equals}&`;
    if (this._contains !== undefined && this._contains !== null) strFilter += `${this.variableName}.contains=${this._contains}&`;
    if (this._doesNotContain !== undefined && this._doesNotContain !== null)
      strFilter += `${this.variableName}.doesNotContain=${this._doesNotContain}&`;

    if (this._in !== undefined && this._in !== null) {
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

  get contains(): string {
    return this._contains;
  }

  set contains(value: string) {
    this._contains = value;
  }

  get doesNotContain(): string {
    return this._doesNotContain;
  }

  set doesNotContain(value: string) {
    this._doesNotContain = value;
  }
}
