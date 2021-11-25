import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'product',
        data: { pageTitle: 'khongGianTruyenApp.product.home.title' },
        loadChildren: () => import('./product/product.module').then(m => m.ProductModule),
      },
      {
        path: 'author',
        data: { pageTitle: 'khongGianTruyenApp.author.home.title' },
        loadChildren: () => import('./author/author.module').then(m => m.AuthorModule),
      },
      {
        path: 'rating',
        data: { pageTitle: 'khongGianTruyenApp.rating.home.title' },
        loadChildren: () => import('./rating/rating.module').then(m => m.RatingModule),
      },
      {
        path: 'comment',
        data: { pageTitle: 'khongGianTruyenApp.comment.home.title' },
        loadChildren: () => import('./comment/comment.module').then(m => m.CommentModule),
      },
      {
        path: 'like',
        data: { pageTitle: 'khongGianTruyenApp.like.home.title' },
        loadChildren: () => import('./like/like.module').then(m => m.LikeModule),
      },
      {
        path: 'download',
        data: { pageTitle: 'khongGianTruyenApp.download.home.title' },
        loadChildren: () => import('./download/download.module').then(m => m.DownloadModule),
      },
      {
        path: 'resource-download',
        data: { pageTitle: 'khongGianTruyenApp.resourceDownload.home.title' },
        loadChildren: () => import('./resource-download/resource-download.module').then(m => m.ResourceDownloadModule),
      },
      {
        path: 'category',
        data: { pageTitle: 'khongGianTruyenApp.category.home.title' },
        loadChildren: () => import('./category/category.module').then(m => m.CategoryModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
