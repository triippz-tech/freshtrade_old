import { ICountry } from 'app/shared/model/country.model';
import { IItem } from 'app/shared/model/item.model';
import { ITradeEvent } from 'app/shared/model/trade-event.model';

export interface ILocation {
  id?: number;
  shortName?: string;
  address?: string | null;
  postalCode?: string | null;
  latLocation?: number | null;
  longLocation?: number | null;
  country?: ICountry | null;
  items?: IItem[] | null;
  tradeEvents?: ITradeEvent[] | null;
}

export const defaultValue: Readonly<ILocation> = {};
