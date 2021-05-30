import dayjs from 'dayjs';
import { IUser } from 'app/shared/model/user.model';
import { ITradeEvent } from 'app/shared/model/trade-event.model';

export interface IReservation {
  id?: string;
  reservationNumber?: string;
  isActive?: boolean;
  isCancelled?: boolean | null;
  cancellationNote?: string | null;
  pickupTime?: string | null;
  createdDate?: string | null;
  updatedDate?: string | null;
  seller?: IUser | null;
  buyer?: IUser | null;
  event?: ITradeEvent | null;
}

export const defaultValue: Readonly<IReservation> = {
  isActive: false,
  isCancelled: false,
};
