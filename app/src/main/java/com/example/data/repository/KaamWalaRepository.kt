package com.example.data.repository

import com.example.data.local.WorkerDao
import com.example.data.local.WorkerEntity
import com.example.data.local.WorkRequestDao
import com.example.data.local.WorkRequestEntity
import com.example.data.model.EscrowStatus
import com.example.data.model.RequestStatus
import com.example.data.model.ServiceCategory
import com.example.data.model.VerificationLevel
import kotlinx.coroutines.flow.Flow

class KaamWalaRepository(
    private val workerDao: WorkerDao,
    private val workRequestDao: WorkRequestDao
) {
    val allWorkers: Flow<List<WorkerEntity>> = workerDao.getAllWorkers()
    val allRequests: Flow<List<WorkRequestEntity>> = workRequestDao.getAllRequests()

    fun getWorkersByCategory(category: ServiceCategory): Flow<List<WorkerEntity>> {
        return if (category == ServiceCategory.ALL) {
            workerDao.getAllWorkers()
        } else {
            workerDao.getWorkersByCategory(category)
        }
    }

    fun searchWorkers(query: String): Flow<List<WorkerEntity>> {
        return workerDao.searchWorkers(query)
    }

    fun getWorkerById(id: Long): Flow<WorkerEntity?> {
        return workerDao.getWorkerById(id)
    }

    fun getRequestsForWorker(workerId: Long): Flow<List<WorkRequestEntity>> {
        return workRequestDao.getRequestsForWorker(workerId)
    }

    suspend fun createWorkRequest(request: WorkRequestEntity): Long {
        return workRequestDao.insertRequest(request)
    }

    suspend fun updateRequestStatus(
        requestId: Long,
        newStatus: RequestStatus,
        newEscrowStatus: EscrowStatus? = null
    ) {
        // Fetch current and update
    }

    suspend fun updateRequest(request: WorkRequestEntity) {
        workRequestDao.updateRequest(request)
    }

    suspend fun updateWorker(worker: WorkerEntity) {
        workerDao.updateWorker(worker)
    }

    suspend fun registerWorker(worker: WorkerEntity): Long {
        return workerDao.insertWorker(worker)
    }

    suspend fun seedDatabaseIfEmpty() {
        if (workerDao.getWorkerCount() == 0) {
            val sampleWorkers = listOf(
                WorkerEntity(
                    name = "Ramesh Mistri",
                    phone = "+91 98765 43210",
                    category = ServiceCategory.MISTRI,
                    skills = "Brickwork, Wall Plaster, Chhat Dhalai, Floor Paving, Boundary Wall",
                    experienceYears = 14,
                    rating = 4.9f,
                    totalReviews = 84,
                    qualityRating = 4.9f,
                    behaviourRating = 5.0f,
                    timeRating = 4.8f,
                    priceRating = 4.8f,
                    locationArea = "Sector 18, Central City",
                    distanceKm = 1.4f,
                    hourlyRate = 650,
                    visitCharge = 150,
                    isAvailable = true,
                    verificationLevel = VerificationLevel.TRUSTED_WORKER,
                    completedJobsCount = 142,
                    bio = "14 saal se pakka mason aur civil repairing ka anubhav. Ghar ki deewar, plastering aur renovation ka sabhi kaam guarantee ke sath.",
                    isEmergencyServiceAvailable = true,
                    avatarInitials = "RM",
                    toolsProvided = "Trowel, Level pipe, Chisel, Concrete mixer contact"
                ),
                WorkerEntity(
                    name = "Suraj Sharma",
                    phone = "+91 98112 33445",
                    category = ServiceCategory.ELECTRICIAN,
                    skills = "Short Circuit Repair, MCB Tripping, Inverter Fitting, House Wiring, Ceiling Fan",
                    experienceYears = 8,
                    rating = 4.8f,
                    totalReviews = 112,
                    qualityRating = 4.9f,
                    behaviourRating = 4.8f,
                    timeRating = 4.9f,
                    priceRating = 4.7f,
                    locationArea = "Civil Lines, Market Area",
                    distanceKm = 0.8f,
                    hourlyRate = 400,
                    visitCharge = 100,
                    isAvailable = true,
                    verificationLevel = VerificationLevel.TRUSTED_WORKER,
                    completedJobsCount = 210,
                    bio = "Certified Industrial & Home Electrician. Safe wiring, load calculation aur quick fault troubleshooting. 24x7 emergency service available.",
                    isEmergencyServiceAvailable = true,
                    avatarInitials = "SS",
                    toolsProvided = "Multimeter, Insulation Tester, Drill machine, Wire puller"
                ),
                WorkerEntity(
                    name = "Manoj Verma",
                    phone = "+91 97654 88990",
                    category = ServiceCategory.PLUMBER,
                    skills = "Pipe Leakage, Tap Fitting, Submersible Pump, Concealed Piping, Cistern Repair",
                    experienceYears = 9,
                    rating = 4.7f,
                    totalReviews = 67,
                    qualityRating = 4.8f,
                    behaviourRating = 4.7f,
                    timeRating = 4.6f,
                    priceRating = 4.7f,
                    locationArea = "Indiranagar, Block B",
                    distanceKm = 2.1f,
                    hourlyRate = 450,
                    visitCharge = 120,
                    isAvailable = true,
                    verificationLevel = VerificationLevel.SKILL_VERIFIED,
                    completedJobsCount = 98,
                    bio = "Bathroom fittings, drainage blockages aur overhead water tank cleaning ka expert. Kaam safai aur bina extra kharch ke.",
                    isEmergencyServiceAvailable = true,
                    avatarInitials = "MV",
                    toolsProvided = "Pipe wrench, Pressure test pump, Thread sealers, Drain auger"
                ),
                WorkerEntity(
                    name = "Vikram Singh",
                    phone = "+91 96541 22334",
                    category = ServiceCategory.CARPENTER,
                    skills = "Door Locks & Latches, Modular Kitchen, Bed & Almirah Repair, Wooden Partition",
                    experienceYears = 11,
                    rating = 4.9f,
                    totalReviews = 76,
                    qualityRating = 5.0f,
                    behaviourRating = 4.9f,
                    timeRating = 4.7f,
                    priceRating = 4.6f,
                    locationArea = "Gomti Nagar, Ext 4",
                    distanceKm = 3.2f,
                    hourlyRate = 500,
                    visitCharge = 150,
                    isAvailable = false,
                    verificationLevel = VerificationLevel.TRUSTED_WORKER,
                    completedJobsCount = 135,
                    bio = "Teak wood, ply, mica aur hardware fittings ka master craftsman. Purane furniture ki repair aur naye designs ka customised kaam.",
                    isEmergencyServiceAvailable = false,
                    avatarInitials = "VS",
                    toolsProvided = "Circular saw, Planner, Router, Precision clamp sets"
                ),
                WorkerEntity(
                    name = "Ajay Kumar Painter",
                    phone = "+91 95432 11223",
                    category = ServiceCategory.PAINTER,
                    skills = "Wall Putty, Royal Luxury Paint, Waterproofing, Texture Art, Exterior Weatherproof",
                    experienceYears = 7,
                    rating = 4.6f,
                    totalReviews = 49,
                    qualityRating = 4.7f,
                    behaviourRating = 4.8f,
                    timeRating = 4.5f,
                    priceRating = 4.8f,
                    locationArea = "Vaishali, Sector 3",
                    distanceKm = 1.9f,
                    hourlyRate = 400,
                    visitCharge = 100,
                    isAvailable = true,
                    verificationLevel = VerificationLevel.SKILL_VERIFIED,
                    completedJobsCount = 74,
                    bio = "Deewaron ki seelan (dampness) ka permanent solution aur modern interior color combination. Paint material bhi wholesale rate par provide karate hain.",
                    isEmergencyServiceAvailable = false,
                    avatarInitials = "AK",
                    toolsProvided = "Roller sets, Airless spray gun, Sanding machine, Ladder"
                ),
                WorkerEntity(
                    name = "Imran Khan Tile Specialist",
                    phone = "+91 94321 99887",
                    category = ServiceCategory.TILE_WORKER,
                    skills = "Floor Tiles, Vitrified Nano, Marble Cutting, Bathroom Wall Tiles, Epoxy Grouting",
                    experienceYears = 10,
                    rating = 4.8f,
                    totalReviews = 58,
                    qualityRating = 4.9f,
                    behaviourRating = 4.7f,
                    timeRating = 4.8f,
                    priceRating = 4.7f,
                    locationArea = "Andheri Colony, Pocket A",
                    distanceKm = 2.7f,
                    hourlyRate = 550,
                    visitCharge = 150,
                    isAvailable = true,
                    verificationLevel = VerificationLevel.TRUSTED_WORKER,
                    completedJobsCount = 110,
                    bio = "Laser leveling ke sath tiles fitting. Zero gap aur waterproof tile fixing. Italian marble aur granite slab fixing ka anubhav.",
                    isEmergencyServiceAvailable = false,
                    avatarInitials = "IK",
                    toolsProvided = "Laser level gauge, Tile cutter machine, Suction lifters"
                ),
                WorkerEntity(
                    name = "Geeta Deep Cleaning Squad",
                    phone = "+91 93210 77665",
                    category = ServiceCategory.CLEANER,
                    skills = "Full Home Deep Clean, Kitchen Chimney De-greasing, Bathroom Acid-free wash, Sofa Shampooing",
                    experienceYears = 6,
                    rating = 4.9f,
                    totalReviews = 92,
                    qualityRating = 5.0f,
                    behaviourRating = 5.0f,
                    timeRating = 4.9f,
                    priceRating = 4.8f,
                    locationArea = "Sector 62, Green Park",
                    distanceKm = 1.2f,
                    hourlyRate = 600,
                    visitCharge = 100,
                    isAvailable = true,
                    verificationLevel = VerificationLevel.TRUSTED_WORKER,
                    completedJobsCount = 160,
                    bio = "Verified professional team. Non-toxic, child-safe chemicals aur industrial vacuum cleaner se complete home sanitization aur deep shine.",
                    isEmergencyServiceAvailable = true,
                    avatarInitials = "GD",
                    toolsProvided = "Wet/Dry Vacuum, Steam cleaner, Microfiber tools, Floor scrubber"
                ),
                WorkerEntity(
                    name = "Santosh Yadav",
                    phone = "+91 92109 55443",
                    category = ServiceCategory.APPLIANCE_REPAIR,
                    skills = "Split AC Gas Refill, Washing Machine PCB Repair, Geyser Coil Replacement, Microwave",
                    experienceYears = 8,
                    rating = 4.7f,
                    totalReviews = 81,
                    qualityRating = 4.8f,
                    behaviourRating = 4.6f,
                    timeRating = 4.8f,
                    priceRating = 4.6f,
                    locationArea = "Dwarka, Sector 10",
                    distanceKm = 3.5f,
                    hourlyRate = 450,
                    visitCharge = 150,
                    isAvailable = true,
                    verificationLevel = VerificationLevel.SKILL_VERIFIED,
                    completedJobsCount = 128,
                    bio = "All brands authorized parts service. 30-day post-service warranty with digital bill on every appliance repair.",
                    isEmergencyServiceAvailable = true,
                    avatarInitials = "SY",
                    toolsProvided = "Gas manifold gauge, Welding torch, Capacitance meter"
                ),
                WorkerEntity(
                    name = "Dinesh Sahu Mechanic",
                    phone = "+91 91098 44332",
                    category = ServiceCategory.MECHANIC,
                    skills = "Two-Wheeler Onsite Breakdown, Generator Servicing, Water Pump Motor Winding",
                    experienceYears = 12,
                    rating = 4.8f,
                    totalReviews = 63,
                    qualityRating = 4.8f,
                    behaviourRating = 4.9f,
                    timeRating = 4.9f,
                    priceRating = 4.7f,
                    locationArea = "Station Road, Old City",
                    distanceKm = 1.8f,
                    hourlyRate = 350,
                    visitCharge = 100,
                    isAvailable = true,
                    verificationLevel = VerificationLevel.IDENTITY_VERIFIED,
                    completedJobsCount = 95,
                    bio = "Ghar par aakar bike/scooter ki service aur breakdown support. Punctual aur genuine parts use karne ki guarantee.",
                    isEmergencyServiceAvailable = true,
                    avatarInitials = "DS",
                    toolsProvided = "Mobile tool kit, Battery jump cables, Spark plug tester"
                )
            )
            workerDao.insertWorkers(sampleWorkers)

            // Seed 2 initial work requests to demonstrate TrustX Escrow lifecycle
            val sampleRequests = listOf(
                WorkRequestEntity(
                    workerId = 2,
                    workerName = "Suraj Sharma",
                    workerCategory = ServiceCategory.ELECTRICIAN,
                    workerPhone = "+91 98112 33445",
                    customerName = "Rahul Verma",
                    customerPhone = "+91 98980 12345",
                    customerAddress = "Flat 402, Shivalik Heights, Sector 18",
                    taskTitle = "Main Switchboard Sparking & Inverter Repair",
                    taskDescription = "Kitchen switchboard se smoke aayi thi, MCB baar baar trip ho rahi hai. Kripya check karein.",
                    jobType = "Emergency Service",
                    scheduledDate = "Today",
                    scheduledTimeSlot = "11:30 AM - 12:30 PM",
                    estimatedAmount = 550,
                    finalAmount = 550,
                    requestStatus = RequestStatus.IN_PROGRESS,
                    escrowStatus = EscrowStatus.SECURED_IN_ESCROW,
                    completionCode = "KW-4821"
                ),
                WorkRequestEntity(
                    workerId = 3,
                    workerName = "Manoj Verma",
                    workerCategory = ServiceCategory.PLUMBER,
                    workerPhone = "+91 97654 88990",
                    customerName = "Rahul Verma",
                    customerPhone = "+91 98980 12345",
                    customerAddress = "Flat 402, Shivalik Heights, Sector 18",
                    taskTitle = "Bathroom Tap Replacement & Sink Joint Sealing",
                    taskDescription = "Master bathroom me hot water tap se continuous dripping hai aur sink pipe leak ho raha hai.",
                    jobType = "Labour Only",
                    scheduledDate = "Yesterday",
                    scheduledTimeSlot = "04:00 PM",
                    estimatedAmount = 450,
                    finalAmount = 450,
                    requestStatus = RequestStatus.COMPLETED,
                    escrowStatus = EscrowStatus.SECURED_IN_ESCROW,
                    completionCode = "KW-7193"
                )
            )
            sampleRequests.forEach { workRequestDao.insertRequest(it) }
        }
    }
}
