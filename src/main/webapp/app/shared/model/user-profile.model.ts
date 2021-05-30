import { IUser } from 'app/shared/model/user.model';

export interface IUserProfile {
  id?: string;
  location?: string | null;
  description?: string | null;
  user?: IUser | null;
}

export const defaultValue: Readonly<IUserProfile> = {};
