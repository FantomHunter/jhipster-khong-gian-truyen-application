jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IResourceDownload, ResourceDownload } from '../resource-download.model';
import { ResourceDownloadService } from '../service/resource-download.service';

import { ResourceDownloadRoutingResolveService } from './resource-download-routing-resolve.service';

describe('ResourceDownload routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: ResourceDownloadRoutingResolveService;
  let service: ResourceDownloadService;
  let resultResourceDownload: IResourceDownload | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(ResourceDownloadRoutingResolveService);
    service = TestBed.inject(ResourceDownloadService);
    resultResourceDownload = undefined;
  });

  describe('resolve', () => {
    it('should return IResourceDownload returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultResourceDownload = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultResourceDownload).toEqual({ id: 123 });
    });

    it('should return new IResourceDownload if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultResourceDownload = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultResourceDownload).toEqual(new ResourceDownload());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as ResourceDownload })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultResourceDownload = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultResourceDownload).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
