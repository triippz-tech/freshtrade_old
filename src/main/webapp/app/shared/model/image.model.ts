import dayjs from 'dayjs';
import { IItem } from 'app/shared/model/item.model';

export interface IImage {
  id?: string;
  imageUrl?: string | null;
  createdDate?: string | null;
  isVisible?: boolean | null;
  item?: IItem | null;
}

export const defaultValue: Readonly<IImage> = {
  isVisible: false,
};
