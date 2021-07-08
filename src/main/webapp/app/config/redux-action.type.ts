import { AxiosPromise } from 'axios';
import { Criteria } from 'app/shared/criteria/criteria';

export interface IPayload<T> {
  type: string;
  payload: AxiosPromise<T>;
  meta?: any;
}
export type IPayloadResult<T> = (dispatch: any, getState?: any) => IPayload<T> | Promise<IPayload<T>>;
export type ICrudGetActionNoId<T> = () => IPayload<T> | ((dispatch: any) => IPayload<T>);
export type ICrudGetActionWithId<T> = (id: string | number) => IPayload<T> | ((dispatch: any) => IPayload<T>);
export type ICrudGetAllAction<T> = (page?: number, size?: number, sort?: string) => IPayload<T> | ((dispatch: any) => IPayload<T>);
export type ICrudSearchAction<T> = (
  search?: string,
  page?: number,
  size?: number,
  sort?: string
) => IPayload<T> | ((dispatch: any) => IPayload<T>);
export type ICrudLoadOptionsAction<T> = (inputValue: string, callback: Function) => IPayload<T> | IPayloadResult<T>;
export type ICrudPutAction<T> = (data?: T) => IPayload<T> | IPayloadResult<T>;
export type ICrudPutActionCB<T> = (data?: T, callback?: () => void) => IPayload<T> | IPayloadResult<T>;
export type ICrudDeleteAction<T> = (id?: string | number) => IPayload<T> | IPayloadResult<T>;
export type ICrudGetAllActionCriteria<T> = (
  criteria?: Criteria,
  page?: number,
  size?: number,
  sort?: string,
  callback?: () => void
) => IPayload<T> | ((dispatch: any) => IPayload<T>);
export type ICrudReserveAction<T> = (
  data: T,
  quantity: number,
  callback: (status: number, count: number) => void
) => IPayload<T> | IPayloadResult<T>;
