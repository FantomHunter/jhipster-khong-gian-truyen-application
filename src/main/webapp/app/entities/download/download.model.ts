import * as dayjs from 'dayjs';
import { IUser } from 'app/entities/user/user.model';
import { IResourceDownload } from 'app/entities/resource-download/resource-download.model';

export interface IDownload {
  id?: number;
  downloadDate?: dayjs.Dayjs | null;
  user?: IUser;
  resource?: IResourceDownload;
}

export class Download implements IDownload {
  constructor(public id?: number, public downloadDate?: dayjs.Dayjs | null, public user?: IUser, public resource?: IResourceDownload) {}
}

export function getDownloadIdentifier(download: IDownload): number | undefined {
  return download.id;
}
