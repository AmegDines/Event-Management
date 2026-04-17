"use client";

import { Calendar, MapPin, Clock, Users } from "lucide-react";
import { toast } from "sonner";
import { useState } from "react";

interface EventCardProps {
  event: {
    _id: string;
    title: string;
    description: string;
    date: string;
    time: string;
    location: string;
    category: string;
    image?: string;
  };
}

export default function EventCard({ event }: EventCardProps) {
  const [isRSVPing, setIsRSVPing] = useState(false);

  const handleRSVP = async () => {
    setIsRSVPing(true);
    try {
      const res = await fetch(`/api/events/${event._id}/rsvp`, {
        method: "POST",
      });
      const data = await res.json();
      
      if (!res.ok) {
        throw new Error(data.error || "Failed to RSVP");
      }
      
      toast.success("Successfully registered for this event!");
    } catch (error: any) {
      toast.error(error.message);
    } finally {
      setIsRSVPing(false);
    }
  };

  const formattedDate = new Date(event.date).toLocaleDateString("en-US", {
    weekday: "short",
    month: "long",
    day: "numeric",
  });

  return (
    <div className="bg-white rounded-2xl border border-gray-100 shadow-sm overflow-hidden flex flex-col transition-all hover:shadow-md hover:border-gray-200">
      <div className="h-48 bg-gray-100 relative w-full flex items-center justify-center overflow-hidden">
        {event.image ? (
          <img src={event.image} alt={event.title} className="w-full h-full object-cover" />
        ) : (
          <span className="text-gray-400 font-medium">No Image</span>
        )}
        <div className="absolute top-4 left-4 bg-white/90 backdrop-blur px-3 py-1 rounded-full text-xs font-bold text-blue-700 uppercase tracking-wide">
          {event.category}
        </div>
      </div>
      
      <div className="p-5 flex-1 flex flex-col">
        <h3 className="font-bold text-xl text-gray-900 mb-2 line-clamp-1">{event.title}</h3>
        <p className="text-gray-500 text-sm line-clamp-2 mb-4 flex-1">
          {event.description}
        </p>
        
        <div className="space-y-2 mb-6 text-sm text-gray-600">
          <div className="flex items-center">
            <Calendar className="h-4 w-4 mr-2 text-blue-500" />
            <span>{formattedDate}</span>
          </div>
          <div className="flex items-center">
            <Clock className="h-4 w-4 mr-2 text-blue-500" />
            <span>{event.time}</span>
          </div>
          <div className="flex items-center">
            <MapPin className="h-4 w-4 mr-2 text-blue-500" />
            <span className="truncate">{event.location}</span>
          </div>
        </div>
        
        <button
          onClick={handleRSVP}
          disabled={isRSVPing}
          className="w-full py-2.5 bg-sky-50 text-sky-700 hover:bg-sky-100 font-semibold rounded-xl transition-colors disabled:opacity-50"
        >
          {isRSVPing ? "Processing..." : "RSVP Now"}
        </button>
      </div>
    </div>
  );
}
