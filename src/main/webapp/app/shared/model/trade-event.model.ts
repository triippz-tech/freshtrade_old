import dayjs from 'dayjs';
import { ILocation } from 'app/shared/model/location.model';
import { IItem } from 'app/shared/model/item.model';
import { IReservation } from 'app/shared/model/reservation.model';

export interface ITradeEvent {
  id?: string;
  eventName?: string;
  eventDescription?: string;
  startDate?: string;
  endDate?: string;
  isActive?: boolean | null;
  location?: ILocation | null;
  items?: IItem[] | null;
  reservations?: IReservation[] | null;
}

export const defaultValue: Readonly<ITradeEvent> = {
  isActive: false,
};
