import { NextRequest, NextResponse } from "next/server";
import dbConnect from "@/lib/db";
import Registration from "@/models/Registration";
import Event from "@/models/Event";
import { auth } from "@/auth";

export async function POST(
  req: NextRequest,
  { params }: { params: Promise<{ id: string }> }
) {
  try {
    const session = await auth();
    if (!session?.user) {
      return NextResponse.json({ error: "Unauthorized" }, { status: 401 });
    }

    const { id } = await params;

    await dbConnect();
    
    const event = await Event.findById(id);
    if (!event) {
      return NextResponse.json({ error: "Event not found" }, { status: 404 });
    }

    const existingRsvp = await Registration.findOne({
      user: session.user.id,
      event: id,
    });

    if (existingRsvp) {
      return NextResponse.json({ error: "Already registered for this event" }, { status: 400 });
    }

    const registration = await Registration.create({
      user: session.user.id,
      event: id,
      status: "Confirmed",
    });

    return NextResponse.json({ message: "RSVP successful", registration }, { status: 201 });
  } catch (error: any) {
    return NextResponse.json({ error: error.message }, { status: 500 });
  }
}
