import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ResourceDownloadDetailComponent } from './resource-download-detail.component';

describe('ResourceDownload Management Detail Component', () => {
  let comp: ResourceDownloadDetailComponent;
  let fixture: ComponentFixture<ResourceDownloadDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ResourceDownloadDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ resourceDownload: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(ResourceDownloadDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ResourceDownloadDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load resourceDownload on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.resourceDownload).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
