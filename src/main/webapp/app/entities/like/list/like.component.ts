import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ILike } from '../like.model';
import { LikeService } from '../service/like.service';
import { LikeDeleteDialogComponent } from '../delete/like-delete-dialog.component';

@Component({
  selector: 'jhi-like',
  templateUrl: './like.component.html',
})
export class LikeComponent implements OnInit {
  likes?: ILike[];
  isLoading = false;

  constructor(protected likeService: LikeService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.likeService.query().subscribe(
      (res: HttpResponse<ILike[]>) => {
        this.isLoading = false;
        this.likes = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: ILike): number {
    return item.id!;
  }

  delete(like: ILike): void {
    const modalRef = this.modalService.open(LikeDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.like = like;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
