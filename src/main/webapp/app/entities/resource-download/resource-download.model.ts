import { IDownload } from 'app/entities/download/download.model';
import { IProduct } from 'app/entities/product/product.model';
import { FormatType } from 'app/entities/enumerations/format-type.model';

export interface IResourceDownload {
  id?: number;
  url?: string;
  format?: FormatType | null;
  downloads?: IDownload[] | null;
  product?: IProduct;
}

export class ResourceDownload implements IResourceDownload {
  constructor(
    public id?: number,
    public url?: string,
    public format?: FormatType | null,
    public downloads?: IDownload[] | null,
    public product?: IProduct
  ) {}
}

export function getResourceDownloadIdentifier(resourceDownload: IResourceDownload): number | undefined {
  return resourceDownload.id;
}
