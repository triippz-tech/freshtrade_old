import { NumberFilter } from 'app/shared/util/filters/number-filter';
import { StringFilter } from 'app/shared/util/filters/string-filter';
import { BooleanFilter } from 'app/shared/util/filters/boolean-filter';
import { Criteria } from 'app/shared/criteria/criteria';
import { IImage } from 'app/shared/model/image.model';
import { UUIDFilter } from 'app/shared/util/filters/uuid-filter';
import { ConditionFilter } from 'app/shared/util/filters/enum/condition-filter';

export interface IItemCriteria {
  id?: UUIDFilter;
  price?: NumberFilter;
  quantity?: NumberFilter;
  name?: StringFilter;
  details?: StringFilter;
  itemCondition?: ConditionFilter;
  isActive?: BooleanFilter;
  createdDate?: StringFilter;
  updatedDate?: StringFilter;
  imagesId?: UUIDFilter;
  tokensId?: NumberFilter;
  ownerId?: NumberFilter;
  locationId?: NumberFilter;
  tradeEventId?: UUIDFilter;
  categoriesId?: UUIDFilter;
  categorySlug?: StringFilter;
  userId?: NumberFilter;
}

export const defaultFilter: Readonly<IItemCriteria> = {
  id: null,
  price: null,
  quantity: null,
  name: null,
  details: null,
  itemCondition: null,
  isActive: null,
  createdDate: null,
  updatedDate: null,
  imagesId: null,
  tokensId: null,
  ownerId: null,
  locationId: null,
  tradeEventId: null,
  categoriesId: null,
  categorySlug: null,
  userId: null,
};

export interface IItemCriteriaValues {
  id?: string;
  price?: NumberFilter;
  quantity?: NumberFilter;
  name?: string;
  details?: string;
  itemCondition?: ConditionFilter;
  isActive?: BooleanFilter;
  createdDate?: string;
  updatedDate?: string;
  imagesId?: Array<IImage>;
  tokensId?: Array<number>;
  ownerId?: number;
  locationId?: NumberFilter;
  tradeEventId?: string;
  categoriesId?: string;
  categorySlug?: string;
  userId?: NumberFilter;
}

export interface IItemDropDownValues {
  valueItemCondition?: string;
  valueLabLocation?: string;
}

export const defaultDropDownValues: IItemDropDownValues = {
  valueItemCondition: '',
};

export const defaultCriteriaValues: IItemCriteriaValues = {
  id: null,
  price: null,
  quantity: null,
  name: null,
  details: null,
  itemCondition: null,
  isActive: null,
  createdDate: null,
  updatedDate: null,
  imagesId: null,
  tokensId: null,
  ownerId: null,
  locationId: null,
  tradeEventId: null,
  categoriesId: null,
  categorySlug: null,
  userId: null,
};

export class ItemCriteria extends Criteria {
  private _id: UUIDFilter;
  private _price: NumberFilter;
  private _quantity: NumberFilter;
  private _name: StringFilter;
  private _details: StringFilter;
  private _itemCondition: ConditionFilter;
  private _isActive: BooleanFilter;
  private _createdDate: StringFilter;
  private _updatedDate: StringFilter;
  private _imagesId: UUIDFilter;
  private _tokensId: NumberFilter;
  private _ownerId: NumberFilter;
  private _locationId: NumberFilter;
  private _tradeEventId: UUIDFilter;
  private _categoriesId: UUIDFilter;
  private _categorySlug: StringFilter;
  private _userId: NumberFilter;

  constructor(criteria: IItemCriteria) {
    super();
    this._id = criteria.id;
    this._price = criteria.price;
    this._quantity = criteria.quantity;
    this._name = criteria.name;
    this._details = criteria.details;
    this._createdDate = criteria.createdDate;
    this._itemCondition = criteria.itemCondition;
    this._isActive = criteria.isActive;
    this._updatedDate = criteria.updatedDate;
    this._imagesId = criteria.imagesId;
    this._tokensId = criteria.tokensId;
    this._ownerId = criteria.ownerId;
    this._locationId = criteria.locationId;
    this._tradeEventId = criteria.tradeEventId;
    this._categoriesId = criteria.categoriesId;
    this._categorySlug = criteria.categorySlug;
    this._userId = criteria.userId;
  }

  public generateUrl(): string {
    let filterString = '';
    if (this._id !== null) filterString += this._id.generateFilter();
    if (this._price !== null) filterString += this._price.generateFilter();
    if (this._quantity !== null) filterString += this._quantity.generateFilter();
    if (this._name !== null) filterString += this._name.generateFilter();
    if (this._details !== null) filterString += this._details.generateFilter();
    if (this._createdDate !== null) filterString += this._createdDate.generateFilter();
    if (this._itemCondition !== null) filterString += this._itemCondition.generateFilter();
    if (this._isActive !== null) filterString += this._isActive.generateFilter();
    if (this._updatedDate !== null) filterString += this._updatedDate.generateFilter();
    if (this._imagesId !== null) filterString += this._imagesId.generateFilter();
    if (this._tokensId !== null) filterString += this._tokensId.generateFilter();
    if (this._ownerId !== null) filterString += this._ownerId.generateFilter();
    if (this._locationId !== null) filterString += this._locationId.generateFilter();
    if (this._tradeEventId !== null) filterString += this._tradeEventId.generateFilter();
    if (this._categoriesId !== null) filterString += this._categoriesId.generateFilter();
    if (this._categorySlug !== null) filterString += this._categorySlug.generateFilter();
    if (this._userId !== null) filterString += this._userId.generateFilter();

    return this.cleanEnding(filterString);
  }

  protected cleanEnding(inStr: string) {
    if (inStr.endsWith('&')) {
      return inStr.slice(0, -1);
    }
    return inStr;
  }

  get id(): UUIDFilter {
    return this._id;
  }

  set id(value: UUIDFilter) {
    this._id = value;
  }

  get price(): NumberFilter {
    return this._price;
  }

  set price(value: NumberFilter) {
    this._price = value;
  }

  get quantity(): NumberFilter {
    return this._quantity;
  }

  set quantity(value: NumberFilter) {
    this._quantity = value;
  }

  get name(): StringFilter {
    return this._name;
  }

  set name(value: StringFilter) {
    this._name = value;
  }

  get details(): StringFilter {
    return this._details;
  }

  set details(value: StringFilter) {
    this._details = value;
  }

  get itemCondition(): ConditionFilter {
    return this._itemCondition;
  }

  set itemCondition(value: ConditionFilter) {
    this._itemCondition = value;
  }

  get isActive(): BooleanFilter {
    return this._isActive;
  }

  set isActive(value: BooleanFilter) {
    this._isActive = value;
  }

  get createdDate(): StringFilter {
    return this._createdDate;
  }

  set createdDate(value: StringFilter) {
    this._createdDate = value;
  }

  get updatedDate(): StringFilter {
    return this._updatedDate;
  }

  set updatedDate(value: StringFilter) {
    this._updatedDate = value;
  }

  get imagesId(): UUIDFilter {
    return this._imagesId;
  }

  set imagesId(value: UUIDFilter) {
    this._imagesId = value;
  }

  get tokensId(): NumberFilter {
    return this._tokensId;
  }

  set tokensId(value: NumberFilter) {
    this._tokensId = value;
  }

  get ownerId(): NumberFilter {
    return this._ownerId;
  }

  set ownerId(value: NumberFilter) {
    this._ownerId = value;
  }

  get locationId(): NumberFilter {
    return this._locationId;
  }

  set locationId(value: NumberFilter) {
    this._locationId = value;
  }

  get tradeEventId(): UUIDFilter {
    return this._tradeEventId;
  }

  set tradeEventId(value: UUIDFilter) {
    this._tradeEventId = value;
  }

  get categoriesId(): UUIDFilter {
    return this._categoriesId;
  }

  set categoriesId(value: UUIDFilter) {
    this._categoriesId = value;
  }

  get categorySlug(): StringFilter {
    return this._categorySlug;
  }

  set categorySlug(value: StringFilter) {
    this._categorySlug = value;
  }

  get userId(): NumberFilter {
    return this._userId;
  }

  set userId(value: NumberFilter) {
    this._userId = value;
  }
}
