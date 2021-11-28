import * as dayjs from 'dayjs';
import { IUser } from 'app/entities/user/user.model';
import { IProduct } from 'app/entities/product/product.model';

export interface IComment {
  id?: number;
  commentDate?: dayjs.Dayjs | null;
  content?: string;
  user?: IUser;
  product?: IProduct;
}

export class Comment implements IComment {
  constructor(
    public id?: number,
    public commentDate?: dayjs.Dayjs | null,
    public content?: string,
    public user?: IUser,
    public product?: IProduct
  ) {}
}

export function getCommentIdentifier(comment: IComment): number | undefined {
  return comment.id;
}
