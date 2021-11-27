export interface IAuthor {
  id?: number;
  name?: string;
}

export class Author implements IAuthor {
  constructor(public id?: number, public name?: string) {}
}

export function getAuthorIdentifier(author: IAuthor): number | undefined {
  return author.id;
}
