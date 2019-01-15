import { Injectable } from '@angular/core';
import {HttpEvent, HttpRequest, HttpResponse} from '@angular/common/http';

const maxAge = 30000;
@Injectable()
export class RequestCacheService {

  cache = new Map();

  private deleteExpiredEntries() {
    const expired = Date.now() - maxAge;
    this.cache.forEach(entry => {
      if (entry.lastRead < expired) {
        this.cache.delete(entry.url);
      }
    });
  }

  get(req: HttpRequest<any>): HttpEvent<any> | undefined {
    const url = req.urlWithParams;
    const cached = this.cache.get(url);

    // todo - add debounce
    this.deleteExpiredEntries();
    if (!cached) {
      return undefined;
    }

    return cached.response;
  }

  put(req: HttpRequest<any>, response: HttpEvent<any>): void {
    const url = req.url;
    const entry = {
      url,
      response,
      lastRead: Date.now()
    };
    this.cache.set(url, entry);
  }

}


