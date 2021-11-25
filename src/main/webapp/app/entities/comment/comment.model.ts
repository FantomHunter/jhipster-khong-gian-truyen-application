import * as dayjs from 'dayjs';
import { IUser } from 'app/entities/user/user.model';
import { IProduct } from 'app/entities/product/product.model';

export interface IComment {
  id?: number;
  commentDate?: dayjs.Dayjs | null;
  content?: string | null;
  user?: IUser | null;
  product?: IProduct | null;
}

export class Comment implements IComment {
  constructor(
    public id?: number,
    public commentDate?: dayjs.Dayjs | null,
    public content?: string | null,
    public user?: IUser | null,
    public product?: IProduct | null
  ) {}
}

export function getCommentIdentifier(comment: IComment): number | undefined {
  return comment.id;
}
