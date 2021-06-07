// import {INumberFilter, NumberFilter} from "app/shared/util/filters/number-filter";
// import {MomentFilter} from "app/shared/util/filters/moment-filter";
// import {BooleanFilter} from "app/shared/util/filters/boolean-filter";
// import {IStringFilter, StringFilter} from "app/shared/util/filters/string-filter";
// import {IFilter} from "app/shared/util/filters/filter";
// import {Moment} from "moment";
//
// abstract class BaseFilterBuilder {
//   protected _equals: any;
//   protected _notEquals: any;
//   protected _specified: boolean;
//   protected _in: Array<any>;
//
//   protected constructor() {
//     this._equals = null;
//     this._notEquals = null;
//     this._specified = null;
//     this._in = null;
//   }
//
//   protected abstract addEquals(equals: any);
//   protected abstract addNotEquals(notEquals: any);
//   protected abstract addSpecified(specified: boolean);
//   protected abstract addIn(inItem: any);
//   protected abstract build();
//
//   get equals(): any {
//     return this._equals;
//   }
//
//   get notEquals(): any {
//     return this._notEquals;
//   }
//
//   get specified(): boolean {
//     return this._specified;
//   }
//
//   get in(): Array<any> {
//     return this._in;
//   }
//
// }
//
// export class MomentFilterBuilder extends BaseFilterBuilder{
//   constructor() {
//     super();
//   }
//   protected addEquals(equals: Moment) {
//     this._equals = equals;
//     return this;
//   }
//
//   protected addIn(inItem: Moment) {
//     if (this._in === null) this._in = new Array<Moment>();
//     this._in.push(inItem);
//     return this;
//   }
//
//   protected addNotEquals(notEquals: Moment) {
//     this._notEquals = notEquals;
//     return this;
//   }
//
//   protected addSpecified(specified: boolean) {
//     this._specified = specified;
//     return this;
//   }
//
//   protected build() {
//     const filter: IFilter = {
//       equals: this.equals,
//       notEquals: this.notEquals,
//       specified: this.specified,
//       in: this.in
//     }
//     return new MomentFilter(filter);
//   }
// }
// export class NumberFilterBuilder extends BaseFilterBuilder{
//   private _greaterThan: number;
//   private _lessThan: number;
//   private _greaterThanOrEqual: number;
//   private _lessThanOrEqual: number;
//
//   constructor() {
//     super();
//     this._greaterThan = null;
//     this._lessThan = null;
//     this._greaterThanOrEqual = null;
//     this._lessThanOrEqual = null;
//   }
//
//   public greaterThan(greaterThan: number): NumberFilterBuilder {
//     this._greaterThan = greaterThan;
//     return this;
//   }
//   public lessThan(lessThan: number): NumberFilterBuilder {
//     this._lessThan = lessThan;
//     return this;
//   }
//   public greaterThanOrEqual(greaterThanOrEqual: number): NumberFilterBuilder {
//     this._greaterThanOrEqual = greaterThanOrEqual;
//     return this;
//   }
//   public lessThanOrEqual(lessThanOrEqual: number): NumberFilterBuilder {
//     this._lessThanOrEqual = lessThanOrEqual;
//     return this;
//   }
//   protected addEquals(equals: number) {
//     this._equals = equals;
//     return this;
//   }
//   protected addIn(inItem: number) {
//     if (this._in === null) this._in = new Array<number>();
//     this._in.push(inItem);
//     return this;
//   }
//   protected addNotEquals(notEquals: number) {
//     this._notEquals = notEquals;
//     return this;
//   }
//   protected addSpecified(specified: boolean) {
//     this._specified = specified;
//     return this;
//   }
//
//   public build(): NumberFilter {
//     const filter: INumberFilter = {
//       rangeFilter: {
//         greaterThan: this._greaterThan,
//         lessThan: this._lessThan,
//         greaterThanOrEqual: this._greaterThanOrEqual,
//         lessThanOrEqual: this._lessThanOrEqual,
//         filter: {
//           equals: this._equals,
//           notEquals: this._notEquals,
//           specified: this._specified,
//           in: this._in
//         }
//       }
//     }
//     return new NumberFilter(filter);
//   }
//
// }
// export class BooleanFilterBuilder extends BaseFilterBuilder{
//   constructor() {
//     super();
//   }
//   protected addEquals(equals: boolean) {
//     this._equals = equals;
//     return this;
//   }
//
//   protected addIn(inItem: boolean) {
//     if (this._in === null) this._in = new Array<boolean>();
//     this._in.push(inItem);
//     return this;
//   }
//
//   protected addNotEquals(notEquals: boolean) {
//     this._notEquals = notEquals;
//     return this;
//   }
//
//   protected addSpecified(specified: boolean) {
//     this._specified = specified;
//     return this;
//   }
//
//   protected build() {
//     const filter: IFilter = {
//       equals: this.equals,
//       notEquals: this.notEquals,
//       specified: this.specified,
//       in: this.in
//     }
//     return new BooleanFilter(filter);
//   }
// }
// export class StringFilterBuilder extends BaseFilterBuilder{
//   private _contains: string;
//   private _doesNotContain: string;
//
//   constructor() {
//     super();
//     this._contains = null;
//     this._doesNotContain = null;
//   }
//
//   public addContains(contains: string): StringFilterBuilder {
//     this._contains = contains;
//     return this;
//   }
//
//   protected addEquals(equals: string): StringFilterBuilder {
//     this._equals = equals;
//     return this;
//   }
//
//   protected addIn(inItem: string): StringFilterBuilder {
//     if (this._in === null) this._in = new Array<string>();
//     this._in.push(inItem);
//     return this;
//   }
//
//   protected addNotEquals(notEquals: string): StringFilterBuilder {
//     this._notEquals = notEquals;
//     return this;
//   }
//
//   protected addSpecified(specified: boolean): StringFilterBuilder {
//     this._specified = specified;
//     return this;
//   }
//
//
//   get contains(): string {
//     return this._contains;
//   }
//
//   get doesNotContain(): string {
//     return this._doesNotContain;
//   }
//
//   protected build() {
//     const filter: IStringFilter = {
//       contains: this.contains,
//       doesNotContain: this.doesNotContain,
//       filter: {
//         equals: this.equals,
//         notEquals: this.notEquals,
//         specified: this.specified,
//         in: this.in
//       }
//     }
//     return new StringFilter(filter);
//   }
// }
//
// export class FilterBuilder<T> {
//   constructor(classType: T) {
//     if (classType instanceof NumberFilter) {
//       return new NumberFilterBuilder();
//     }
//     if (classType instanceof MomentFilter) {
//       return new MomentFilterBuilder();
//     }
//     if (classType instanceof BooleanFilter) {
//       return new MomentFilterBuilder();
//     }
//     if (classType instanceof StringFilter) {
//       return new StringFilterBuilder();
//     }
//   }
// }
