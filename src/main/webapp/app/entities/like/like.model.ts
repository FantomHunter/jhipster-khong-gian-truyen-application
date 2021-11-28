import * as dayjs from 'dayjs';
import { IUser } from 'app/entities/user/user.model';
import { IProduct } from 'app/entities/product/product.model';

export interface ILike {
  id?: number;
  likeDate?: dayjs.Dayjs | null;
  user?: IUser;
  product?: IProduct;
}

export class Like implements ILike {
  constructor(public id?: number, public likeDate?: dayjs.Dayjs | null, public user?: IUser, public product?: IProduct) {}
}

export function getLikeIdentifier(like: ILike): number | undefined {
  return like.id;
}
