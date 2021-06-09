export interface IFilter<FIELD_TYPE> {
  equals?: FIELD_TYPE;
  notEquals?: FIELD_TYPE;
  specified?: boolean;
  in?: Array<FIELD_TYPE>;
}

export abstract class Filter<FIELD_TYPE> {
  protected _equals: FIELD_TYPE = null;
  protected _notEquals: FIELD_TYPE = null;
  protected _specified: boolean = null;
  protected _in: Array<FIELD_TYPE> = null;

  protected constructor(filter: IFilter<FIELD_TYPE>) {
    this._equals = null;
    this._notEquals = null;
    this._specified = null;
    this._in = null;
    if (filter !== null) {
      this._equals = filter.equals === undefined ? null : filter.equals;
      this._notEquals = filter.notEquals === undefined ? null : filter.notEquals;
      this._specified = filter.specified === undefined ? null : filter.specified;
      this._in = filter.in === undefined ? null : [...filter.in];
    }
  }

  protected abstract generateFilter();

  public getEquals(): FIELD_TYPE {
    return this._equals;
  }

  /**
   *
   * @param equals
   */
  public setEquals(equals: FIELD_TYPE): Filter<FIELD_TYPE> {
    this._equals = equals;
    return this;
  }

  /**
   *
   */
  public getNotEquals(): FIELD_TYPE {
    return this._notEquals;
  }

  /**
   *
   * @param notEquals
   */
  public setNotEquals(notEquals: FIELD_TYPE): Filter<FIELD_TYPE> {
    this._notEquals = notEquals;
    return this;
  }

  /**
   *
   */
  public getSpecified(): boolean {
    return this._specified;
  }

  /**
   *
   * @param specified
   */
  public setSpecified(specified: boolean): Filter<FIELD_TYPE> {
    this._specified = specified;
    return this;
  }

  /**
   *
   */
  public getIn(): Array<FIELD_TYPE> {
    return this._in;
  }

  /**
   *
   * @param inArr
   */
  public setIn(inArr: Array<FIELD_TYPE>): Filter<FIELD_TYPE> {
    this._in = inArr;
    return this;
  }

  get equals(): FIELD_TYPE {
    return this._equals;
  }

  set equals(value: FIELD_TYPE) {
    this._equals = value;
  }

  get notEquals(): FIELD_TYPE {
    return this._notEquals;
  }

  set notEquals(value: FIELD_TYPE) {
    this._notEquals = value;
  }

  get specified(): boolean {
    return this._specified;
  }

  set specified(value: boolean) {
    this._specified = value;
  }

  get in(): Array<FIELD_TYPE> {
    return this._in;
  }

  set in(value: Array<FIELD_TYPE>) {
    this._in = value;
  }
}
