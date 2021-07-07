import dayjs from 'dayjs';
import { IUser } from 'app/shared/model/user.model';
import { ITradeEvent } from 'app/shared/model/trade-event.model';
import { IItemToken } from 'app/shared/model/item-token.model';
import { IItem } from 'app/shared/model/item.model';

export interface IReservation {
  id?: string;
  reservationNumber?: string;
  totalPrice?: number;
  pricePer?: number;
  isActive?: boolean;
  isCancelled?: boolean | null;
  cancellationNote?: string | null;
  pickupTime?: string | null;
  createdDate?: string | null;
  updatedDate?: string | null;
  item?: IItem | null;
  seller?: IUser | null;
  buyer?: IUser | null;
  event?: ITradeEvent | null;
  tokens?: IItemToken[] | null;
}

export const defaultValue: Readonly<IReservation> = {
  isActive: false,
  isCancelled: false,
};
