import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { FormatType } from 'app/entities/enumerations/format-type.model';
import { IResourceDownload, ResourceDownload } from '../resource-download.model';

import { ResourceDownloadService } from './resource-download.service';

describe('ResourceDownload Service', () => {
  let service: ResourceDownloadService;
  let httpMock: HttpTestingController;
  let elemDefault: IResourceDownload;
  let expectedResult: IResourceDownload | IResourceDownload[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ResourceDownloadService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      url: 'AAAAAAA',
      format: FormatType.PRC,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign({}, elemDefault);

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a ResourceDownload', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new ResourceDownload()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ResourceDownload', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          url: 'BBBBBB',
          format: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ResourceDownload', () => {
      const patchObject = Object.assign(
        {
          format: 'BBBBBB',
        },
        new ResourceDownload()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ResourceDownload', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          url: 'BBBBBB',
          format: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a ResourceDownload', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addResourceDownloadToCollectionIfMissing', () => {
      it('should add a ResourceDownload to an empty array', () => {
        const resourceDownload: IResourceDownload = { id: 123 };
        expectedResult = service.addResourceDownloadToCollectionIfMissing([], resourceDownload);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(resourceDownload);
      });

      it('should not add a ResourceDownload to an array that contains it', () => {
        const resourceDownload: IResourceDownload = { id: 123 };
        const resourceDownloadCollection: IResourceDownload[] = [
          {
            ...resourceDownload,
          },
          { id: 456 },
        ];
        expectedResult = service.addResourceDownloadToCollectionIfMissing(resourceDownloadCollection, resourceDownload);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ResourceDownload to an array that doesn't contain it", () => {
        const resourceDownload: IResourceDownload = { id: 123 };
        const resourceDownloadCollection: IResourceDownload[] = [{ id: 456 }];
        expectedResult = service.addResourceDownloadToCollectionIfMissing(resourceDownloadCollection, resourceDownload);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(resourceDownload);
      });

      it('should add only unique ResourceDownload to an array', () => {
        const resourceDownloadArray: IResourceDownload[] = [{ id: 123 }, { id: 456 }, { id: 4601 }];
        const resourceDownloadCollection: IResourceDownload[] = [{ id: 123 }];
        expectedResult = service.addResourceDownloadToCollectionIfMissing(resourceDownloadCollection, ...resourceDownloadArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const resourceDownload: IResourceDownload = { id: 123 };
        const resourceDownload2: IResourceDownload = { id: 456 };
        expectedResult = service.addResourceDownloadToCollectionIfMissing([], resourceDownload, resourceDownload2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(resourceDownload);
        expect(expectedResult).toContain(resourceDownload2);
      });

      it('should accept null and undefined values', () => {
        const resourceDownload: IResourceDownload = { id: 123 };
        expectedResult = service.addResourceDownloadToCollectionIfMissing([], null, resourceDownload, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(resourceDownload);
      });

      it('should return initial array if no ResourceDownload is added', () => {
        const resourceDownloadCollection: IResourceDownload[] = [{ id: 123 }];
        expectedResult = service.addResourceDownloadToCollectionIfMissing(resourceDownloadCollection, undefined, null);
        expect(expectedResult).toEqual(resourceDownloadCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
