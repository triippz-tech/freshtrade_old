import { ILocation } from 'app/shared/model/location.model';

export interface ICountry {
  id?: number;
  countryCode?: string | null;
  countryName?: string | null;
  locations?: ILocation[] | null;
}

export const defaultValue: Readonly<ICountry> = {};
