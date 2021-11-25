import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ILike, getLikeIdentifier } from '../like.model';

export type EntityResponseType = HttpResponse<ILike>;
export type EntityArrayResponseType = HttpResponse<ILike[]>;

@Injectable({ providedIn: 'root' })
export class LikeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/likes');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(like: ILike): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(like);
    return this.http
      .post<ILike>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(like: ILike): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(like);
    return this.http
      .put<ILike>(`${this.resourceUrl}/${getLikeIdentifier(like) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(like: ILike): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(like);
    return this.http
      .patch<ILike>(`${this.resourceUrl}/${getLikeIdentifier(like) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ILike>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ILike[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addLikeToCollectionIfMissing(likeCollection: ILike[], ...likesToCheck: (ILike | null | undefined)[]): ILike[] {
    const likes: ILike[] = likesToCheck.filter(isPresent);
    if (likes.length > 0) {
      const likeCollectionIdentifiers = likeCollection.map(likeItem => getLikeIdentifier(likeItem)!);
      const likesToAdd = likes.filter(likeItem => {
        const likeIdentifier = getLikeIdentifier(likeItem);
        if (likeIdentifier == null || likeCollectionIdentifiers.includes(likeIdentifier)) {
          return false;
        }
        likeCollectionIdentifiers.push(likeIdentifier);
        return true;
      });
      return [...likesToAdd, ...likeCollection];
    }
    return likeCollection;
  }

  protected convertDateFromClient(like: ILike): ILike {
    return Object.assign({}, like, {
      likeDate: like.likeDate?.isValid() ? like.likeDate.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.likeDate = res.body.likeDate ? dayjs(res.body.likeDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((like: ILike) => {
        like.likeDate = like.likeDate ? dayjs(like.likeDate) : undefined;
      });
    }
    return res;
  }
}
