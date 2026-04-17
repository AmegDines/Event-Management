import dbConnect from "@/lib/db";
import Event from "@/models/Event";
import EventCard from "@/components/events/EventCard";

// Enable revalidation or dynamic fetching
export const dynamic = 'force-dynamic';

export default async function EventsPage() {
  await dbConnect();
  
  // Fetch all events
  const eventsData = await Event.find().sort({ date: 1 });
  const events = JSON.parse(JSON.stringify(eventsData)); // Convert mongoose object for client component

  return (
    <div className="w-full">
      <header className="mb-8 flex justify-between items-center bg-white p-6 rounded-2xl shadow-sm border border-gray-100">
        <div>
          <h1 className="text-3xl font-bold text-gray-900">Events</h1>
          <p className="text-gray-500 mt-1">Discover and register for upcoming events.</p>
        </div>
      </header>

      {events.length === 0 ? (
        <div className="bg-white rounded-2xl border border-gray-100 p-12 text-center shadow-sm">
          <div className="h-20 w-20 bg-blue-50 rounded-full flex items-center justify-center mx-auto mb-4">
            <span className="text-2xl">🌍</span>
          </div>
          <h2 className="text-xl font-bold text-gray-900 mb-2">No events found</h2>
          <p className="text-gray-500">There are no upcoming events at the moment. Check back later!</p>
        </div>
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {events.map((event: any) => (
            <EventCard key={event._id} event={event} />
          ))}
        </div>
      )}
    </div>
  );
}
