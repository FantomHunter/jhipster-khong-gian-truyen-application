export interface IAuthor {
  id?: number;
  name?: string | null;
}

export class Author implements IAuthor {
  constructor(public id?: number, public name?: string | null) {}
}

export function getAuthorIdentifier(author: IAuthor): number | undefined {
  return author.id;
}
