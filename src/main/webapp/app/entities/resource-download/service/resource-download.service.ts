import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IResourceDownload, getResourceDownloadIdentifier } from '../resource-download.model';

export type EntityResponseType = HttpResponse<IResourceDownload>;
export type EntityArrayResponseType = HttpResponse<IResourceDownload[]>;

@Injectable({ providedIn: 'root' })
export class ResourceDownloadService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/resource-downloads');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(resourceDownload: IResourceDownload): Observable<EntityResponseType> {
    return this.http.post<IResourceDownload>(this.resourceUrl, resourceDownload, { observe: 'response' });
  }

  update(resourceDownload: IResourceDownload): Observable<EntityResponseType> {
    return this.http.put<IResourceDownload>(
      `${this.resourceUrl}/${getResourceDownloadIdentifier(resourceDownload) as number}`,
      resourceDownload,
      { observe: 'response' }
    );
  }

  partialUpdate(resourceDownload: IResourceDownload): Observable<EntityResponseType> {
    return this.http.patch<IResourceDownload>(
      `${this.resourceUrl}/${getResourceDownloadIdentifier(resourceDownload) as number}`,
      resourceDownload,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IResourceDownload>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IResourceDownload[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addResourceDownloadToCollectionIfMissing(
    resourceDownloadCollection: IResourceDownload[],
    ...resourceDownloadsToCheck: (IResourceDownload | null | undefined)[]
  ): IResourceDownload[] {
    const resourceDownloads: IResourceDownload[] = resourceDownloadsToCheck.filter(isPresent);
    if (resourceDownloads.length > 0) {
      const resourceDownloadCollectionIdentifiers = resourceDownloadCollection.map(
        resourceDownloadItem => getResourceDownloadIdentifier(resourceDownloadItem)!
      );
      const resourceDownloadsToAdd = resourceDownloads.filter(resourceDownloadItem => {
        const resourceDownloadIdentifier = getResourceDownloadIdentifier(resourceDownloadItem);
        if (resourceDownloadIdentifier == null || resourceDownloadCollectionIdentifiers.includes(resourceDownloadIdentifier)) {
          return false;
        }
        resourceDownloadCollectionIdentifiers.push(resourceDownloadIdentifier);
        return true;
      });
      return [...resourceDownloadsToAdd, ...resourceDownloadCollection];
    }
    return resourceDownloadCollection;
  }
}
