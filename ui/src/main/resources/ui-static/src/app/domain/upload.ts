export class Upload<R> {
  data: R;
  value = 0;
  timeId = 0;
  state = 'binding';

  width(): string {
    return this.value + '%';
  }
}
