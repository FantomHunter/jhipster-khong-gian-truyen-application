import * as dayjs from 'dayjs';
import { IUser } from 'app/entities/user/user.model';
import { IProduct } from 'app/entities/product/product.model';

export interface IRating {
  id?: number;
  ratingDate?: dayjs.Dayjs | null;
  value?: number | null;
  user?: IUser | null;
  rating?: IProduct | null;
}

export class Rating implements IRating {
  constructor(
    public id?: number,
    public ratingDate?: dayjs.Dayjs | null,
    public value?: number | null,
    public user?: IUser | null,
    public rating?: IProduct | null
  ) {}
}

export function getRatingIdentifier(rating: IRating): number | undefined {
  return rating.id;
}
