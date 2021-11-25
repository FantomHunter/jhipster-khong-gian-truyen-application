import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ILike, Like } from '../like.model';

import { LikeService } from './like.service';

describe('Like Service', () => {
  let service: LikeService;
  let httpMock: HttpTestingController;
  let elemDefault: ILike;
  let expectedResult: ILike | ILike[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(LikeService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      likeDate: currentDate,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          likeDate: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Like', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          likeDate: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          likeDate: currentDate,
        },
        returnedFromService
      );

      service.create(new Like()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Like', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          likeDate: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          likeDate: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Like', () => {
      const patchObject = Object.assign({}, new Like());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          likeDate: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Like', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          likeDate: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          likeDate: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Like', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addLikeToCollectionIfMissing', () => {
      it('should add a Like to an empty array', () => {
        const like: ILike = { id: 123 };
        expectedResult = service.addLikeToCollectionIfMissing([], like);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(like);
      });

      it('should not add a Like to an array that contains it', () => {
        const like: ILike = { id: 123 };
        const likeCollection: ILike[] = [
          {
            ...like,
          },
          { id: 456 },
        ];
        expectedResult = service.addLikeToCollectionIfMissing(likeCollection, like);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Like to an array that doesn't contain it", () => {
        const like: ILike = { id: 123 };
        const likeCollection: ILike[] = [{ id: 456 }];
        expectedResult = service.addLikeToCollectionIfMissing(likeCollection, like);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(like);
      });

      it('should add only unique Like to an array', () => {
        const likeArray: ILike[] = [{ id: 123 }, { id: 456 }, { id: 20505 }];
        const likeCollection: ILike[] = [{ id: 123 }];
        expectedResult = service.addLikeToCollectionIfMissing(likeCollection, ...likeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const like: ILike = { id: 123 };
        const like2: ILike = { id: 456 };
        expectedResult = service.addLikeToCollectionIfMissing([], like, like2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(like);
        expect(expectedResult).toContain(like2);
      });

      it('should accept null and undefined values', () => {
        const like: ILike = { id: 123 };
        expectedResult = service.addLikeToCollectionIfMissing([], null, like, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(like);
      });

      it('should return initial array if no Like is added', () => {
        const likeCollection: ILike[] = [{ id: 123 }];
        expectedResult = service.addLikeToCollectionIfMissing(likeCollection, undefined, null);
        expect(expectedResult).toEqual(likeCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
