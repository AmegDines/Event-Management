import Image from "next/image";
import AuthModal from "@/components/auth/AuthModal";

export default function Home() {
  return (
    <div className="flex flex-col min-h-screen bg-sky-50 relative overflow-hidden">
      {/* Decorative background elements */}
      <div className="absolute top-[-10%] left-[-10%] w-[40%] h-[40%] rounded-full bg-blue-200 blur-3xl opacity-50 mix-blend-multiply" />
      <div className="absolute bottom-[-10%] right-[-5%] w-[50%] h-[50%] rounded-full bg-sky-200 blur-3xl opacity-50 mix-blend-multiply" />
      <div className="absolute top-[20%] right-[10%] w-[30%] h-[30%] rounded-full bg-indigo-200 blur-3xl opacity-30 mix-blend-multiply" />

      <main className="flex-1 flex flex-col justify-center items-center p-6 relative z-10">
        <div className="text-center mb-10">
          <div className="inline-flex items-center gap-2 mb-4 bg-white/50 backdrop-blur-sm px-4 py-1.5 rounded-full border border-sky-100 shadow-sm text-sm font-medium text-blue-700">
            <span className="relative flex h-2 w-2">
              <span className="animate-ping absolute inline-flex h-full w-full rounded-full bg-blue-400 opacity-75"></span>
              <span className="relative inline-flex rounded-full h-2 w-2 bg-blue-500"></span>
            </span>
            Event Manager 2.0
          </div>
          <h1 className="text-5xl md:text-6xl font-extrabold tracking-tight text-gray-900 mb-4 drop-shadow-sm">
            Manage events with <span className="text-transparent bg-clip-text bg-gradient-to-r from-blue-600 to-indigo-600">ease</span>.
          </h1>
          <p className="text-lg md:text-xl text-gray-600 max-w-2xl mx-auto drop-shadow-sm">
            The all-in-one platform for creating, managing, and attending extraordinary events.
          </p>
        </div>

        <AuthModal />
      </main>
    </div>
  );
}
