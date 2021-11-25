import * as dayjs from 'dayjs';
import { IAuthor } from 'app/entities/author/author.model';
import { IResourceDownload } from 'app/entities/resource-download/resource-download.model';
import { ILike } from 'app/entities/like/like.model';
import { IComment } from 'app/entities/comment/comment.model';
import { IRating } from 'app/entities/rating/rating.model';
import { ICategory } from 'app/entities/category/category.model';
import { Status } from 'app/entities/enumerations/status.model';
import { FormatType } from 'app/entities/enumerations/format-type.model';

export interface IProduct {
  id?: number;
  name?: string | null;
  description?: string | null;
  imageUrl?: string | null;
  publishDate?: dayjs.Dayjs | null;
  status?: Status | null;
  type?: FormatType | null;
  totalChapter?: number | null;
  author?: IAuthor | null;
  resourceDownloads?: IResourceDownload[] | null;
  likes?: ILike[] | null;
  comments?: IComment[] | null;
  ratings?: IRating[] | null;
  categories?: ICategory[] | null;
}

export class Product implements IProduct {
  constructor(
    public id?: number,
    public name?: string | null,
    public description?: string | null,
    public imageUrl?: string | null,
    public publishDate?: dayjs.Dayjs | null,
    public status?: Status | null,
    public type?: FormatType | null,
    public totalChapter?: number | null,
    public author?: IAuthor | null,
    public resourceDownloads?: IResourceDownload[] | null,
    public likes?: ILike[] | null,
    public comments?: IComment[] | null,
    public ratings?: IRating[] | null,
    public categories?: ICategory[] | null
  ) {}
}

export function getProductIdentifier(product: IProduct): number | undefined {
  return product.id;
}
