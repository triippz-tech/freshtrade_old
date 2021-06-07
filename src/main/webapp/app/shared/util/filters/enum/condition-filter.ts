import { Filter, IFilter } from 'app/shared/util/filters/filter';
import { Condition } from 'app/shared/model/enumerations/condition.model';

export interface IConditionFilter {
  filter: IFilter<Condition>;
}

export class ConditionFilter extends Filter<Condition> {
  constructor(obj: IConditionFilter) {
    super(obj.filter);
  }

  public generateFilter() {
    let conditionFilter = '';
    if (this._specified !== undefined && this._specified !== null) conditionFilter += `itemCondition.specified=${this._specified}&`;
    if (this._notEquals !== undefined && this._notEquals !== null) conditionFilter += `itemCondition.notEquals=${this._notEquals}&`;
    if (this._equals !== undefined && this._equals !== null) conditionFilter += `itemCondition.equals=${this._equals}&`;

    if (this._in !== undefined && this._in !== null) {
      this._in.forEach(value => {
        conditionFilter += `itemCondition.in=${value}&`;
      });
    }
    return conditionFilter;
  }
}
