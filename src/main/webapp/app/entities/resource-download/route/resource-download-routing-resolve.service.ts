import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IResourceDownload, ResourceDownload } from '../resource-download.model';
import { ResourceDownloadService } from '../service/resource-download.service';

@Injectable({ providedIn: 'root' })
export class ResourceDownloadRoutingResolveService implements Resolve<IResourceDownload> {
  constructor(protected service: ResourceDownloadService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IResourceDownload> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((resourceDownload: HttpResponse<ResourceDownload>) => {
          if (resourceDownload.body) {
            return of(resourceDownload.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ResourceDownload());
  }
}
