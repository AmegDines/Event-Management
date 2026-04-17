import { auth } from "@/auth";

export default async function DashboardPage() {
  const session = await auth();

  return (
    <div className="w-full">
      <header className="mb-8">
        <h1 className="text-3xl font-bold text-gray-900">Dashboard</h1>
        <p className="text-gray-500 mt-1">Welcome back, {session?.user?.name}</p>
      </header>
      
      <div className="grid grid-cols-1 md:grid-cols-3 gap-6 mb-8">
        <div className="bg-white p-6 rounded-2xl shadow-sm border border-gray-100 flex flex-col justify-between">
          <div>
            <h3 className="text-gray-500 font-medium text-sm mb-1">Total Events</h3>
            <p className="text-3xl font-bold text-gray-900">0</p>
          </div>
          <div className="mt-4 pt-4 border-t border-gray-50">
            <span className="text-sm text-blue-600 font-medium">View all events &rarr;</span>
          </div>
        </div>
        
        <div className="bg-white p-6 rounded-2xl shadow-sm border border-gray-100 flex flex-col justify-between">
          <div>
            <h3 className="text-gray-500 font-medium text-sm mb-1">Upcoming Events</h3>
            <p className="text-3xl font-bold text-gray-900">0</p>
          </div>
          <div className="mt-4 pt-4 border-t border-gray-50">
            <span className="text-sm text-indigo-600 font-medium">Check calendar &rarr;</span>
          </div>
        </div>
        
        <div className="bg-white p-6 rounded-2xl shadow-sm border border-gray-100 flex flex-col justify-between">
          <div>
            <h3 className="text-gray-500 font-medium text-sm mb-1">Registrations</h3>
            <p className="text-3xl font-bold text-gray-900">0</p>
          </div>
          <div className="mt-4 pt-4 border-t border-gray-50">
            <span className="text-sm text-sky-600 font-medium">Manage RSVPs &rarr;</span>
          </div>
        </div>
      </div>

      <div className="bg-white rounded-2xl shadow-sm border border-gray-100 p-6">
        <h2 className="text-xl font-bold text-gray-900 mb-4">Recent Activity</h2>
        <div className="flex flex-col items-center justify-center py-12 text-center">
          <div className="h-16 w-16 bg-gray-50 rounded-full flex items-center justify-center mb-4">
            <span className="text-gray-400">📊</span>
          </div>
          <h3 className="text-gray-900 font-medium mb-1">No activity yet</h3>
          <p className="text-gray-500 text-sm max-w-sm">
            When you create events or users register for them, activity will appear here.
          </p>
        </div>
      </div>
    </div>
  );
}
