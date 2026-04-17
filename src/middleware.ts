import { NextResponse } from "next/server";
import type { NextRequest } from "next/server";

export function middleware(req: NextRequest) {
  const isAuth = req.cookies.get("next-auth.session-token") || req.cookies.get("__Secure-next-auth.session-token") || req.cookies.get("authjs.session-token") || req.cookies.get("__Secure-authjs.session-token");
  const isAuthPage = req.nextUrl.pathname === "/";

  if (isAuthPage) {
    if (isAuth) {
      return NextResponse.redirect(new URL("/dashboard", req.url));
    }
    return null;
  }

  if (!isAuth) {
    let from = req.nextUrl.pathname;
    if (req.nextUrl.search) {
      from += req.nextUrl.search;
    }
    return NextResponse.redirect(
      new URL(`/?from=${encodeURIComponent(from)}`, req.url)
    );
  }
}

export const config = {
  matcher: ["/dashboard/:path*", "/events/:path*", "/"],
};
