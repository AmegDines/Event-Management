import { NextRequest, NextResponse } from "next/server";
import dbConnect from "@/lib/db";
import Event from "@/models/Event";
import { auth } from "@/auth";

export async function GET(req: NextRequest) {
  try {
    await dbConnect();
    
    // Allow public access to GET events so non-logged in users could theoretically view, 
    // or limit it. Based on plan, only logged-in can access dashboard.
    const events = await Event.find()
      .populate("organizer", "name email")
      .sort({ date: 1 });
      
    return NextResponse.json({ events }, { status: 200 });
  } catch (error: any) {
    return NextResponse.json({ error: error.message }, { status: 500 });
  }
}

export async function POST(req: NextRequest) {
  try {
    const session = await auth();
    if (!session?.user) {
      return NextResponse.json({ error: "Unauthorized" }, { status: 401 });
    }

    const body = await req.json();
    const { title, description, date, time, location, category, image } = body;

    await dbConnect();

    const newEvent = await Event.create({
      title,
      description,
      date,
      time,
      location,
      category,
      image,
      organizer: session.user.id,
    });

    return NextResponse.json({ event: newEvent }, { status: 201 });
  } catch (error: any) {
    return NextResponse.json({ error: error.message }, { status: 500 });
  }
}
