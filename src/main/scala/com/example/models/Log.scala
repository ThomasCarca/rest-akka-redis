package com.example.models

import java.time.LocalDateTime

final case class Log(id: String, date: LocalDateTime, level: String, name: String, message: String)
